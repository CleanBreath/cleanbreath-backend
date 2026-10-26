# infra

| 파일 | 내용 |
| --- | --- |
| `ssm-instance-profile.yml` | EC2를 SSM Session Manager로 접속하기 위한 인스턴스 프로파일 |
| `security-baseline.yml` | 멀티 리전 CloudTrail(로그 파일 무결성 검증) + 전용 S3 버킷, GuardDuty, EBS 스냅샷 퍼블릭 액세스 차단 |
| `cloudflare-origin-sg.yml` | Cloudflare IP 프리픽스 리스트와 오리진 SG의 80/443 인바운드 규칙 |
| `account-hardening.md` | CloudFormation으로 못 하는 항목(EBS 기본 암호화, 계정 S3 퍼블릭 액세스 차단, IAM 비밀번호 정책, IMDS hop limit) CLI 절차 |
| `sql/`, `scripts/` | DB 스키마와 데이터 적재 스크립트 |

## 보안 베이스라인 배포 순서

모두 `ap-northeast-2`에서 실행한다. Windows에서는 템플릿의 한글 주석 때문에 `AWS_CLI_FILE_ENCODING=UTF-8`을 붙인다.
배포 전 `aws cloudformation deploy --no-execute-changeset`로 변경 세트를 먼저 확인해도 된다.

### 1. 감사 로그와 위협 탐지 (`security-baseline.yml`)

가장 먼저 배포한다. 이후 작업이 전부 CloudTrail에 남는다.

```bash
AWS_CLI_FILE_ENCODING=UTF-8 aws cloudformation deploy --region ap-northeast-2 \
  --stack-name cleanbreath-security-baseline \
  --template-file infra/security-baseline.yml
```

확인: `aws cloudtrail get-trail-status --name cleanbreath-account-trail` 의 `IsLogging: true`,
`aws guardduty list-detectors --region ap-northeast-2` 에 ID 1개.

예상 비용(서울, 2026-10 Pricing API 기준): CloudTrail 관리 이벤트 첫 사본은 무료이고 S3 저장은 월 0.1 USD 미만.
GuardDuty는 30일 무료 체험 후 대략 월 3~6 USD(CloudTrail 이벤트 4.6 USD/백만 건, VPC Flow/DNS 1.15 USD/GB,
RDS 로그인 모니터링 1.20 USD/vCPU). 정확한 값은 체험 기간 중 GuardDuty 콘솔 Usage 화면의 예상치로 확인한다.

### 2. 계정 하드닝 (`account-hardening.md`)

각 항목을 문서의 CLI로 적용한다. IMDS hop limit은 PickTrip 담당자 확인 후에 적용한다.

### 3. 오리진을 Cloudflare 전용으로 전환 (`cloudflare-origin-sg.yml`)

사이트가 끊기지 않도록 **추가 → 검증 → 제거** 순서를 지킨다.

1. 사전 점검: Cloudflare 대시보드에서 이 EC2 IP를 가리키는 DNS 레코드(CleanBreath, PickTrip 모두)가 전부 **Proxied(주황 구름)** 인지 확인한다.
   DNS only(회색 구름) 레코드, IP로 직접 붙는 외부 모니터링, 오리진에서 HTTP-01로 직접 받는 인증서 갱신이 있으면 4단계 이후 막힌다.
2. 규칙 추가: 스택을 배포한다. 기존 0.0.0.0/0 규칙은 그대로 남으므로 이 단계에서는 아무것도 막히지 않는다.

   ```bash
   AWS_CLI_FILE_ENCODING=UTF-8 aws cloudformation deploy --region ap-northeast-2 \
     --stack-name cleanbreath-cloudflare-origin \
     --template-file infra/cloudflare-origin-sg.yml
   ```

3. 검증:
   - `aws ec2 describe-security-groups --region ap-northeast-2 --group-ids sg-04abc98cb9e5262b8` 에 `PrefixListIds`가 붙은 80/443 규칙이 보이는지 확인
   - 하루 정도 오리진에 실제로 접속한 IP가 전부 Cloudflare 대역인지 확인. 다른 IP가 정상 트래픽이면 원인을 먼저 정리한다.
     CleanBreath 서버 블록은 real_ip(CF-Connecting-IP)로 `$remote_addr`를 사용자 IP로 바꾸므로 로그의 `$remote_addr`가 아니라
     `$realip_remote_addr`(로그 형식에 추가)나 `sudo ss -tn state established '( sport = :443 )'`의 피어 주소로 본다
4. 0.0.0.0/0 제거:

   ```bash
   aws ec2 revoke-security-group-ingress --region ap-northeast-2 --group-id sg-04abc98cb9e5262b8 \
     --ip-permissions 'IpProtocol=tcp,FromPort=80,ToPort=80,IpRanges=[{CidrIp=0.0.0.0/0}]' \
                      'IpProtocol=tcp,FromPort=443,ToPort=443,IpRanges=[{CidrIp=0.0.0.0/0}]'
   ```

5. 사후 확인: 각 도메인을 브라우저와 `curl -I https://<도메인>`으로 열어 본다. 외부에서 `curl -m 5 http://<EC2 퍼블릭 IP>`는 타임아웃이 나야 정상이다.
6. 롤백(문제가 생기면 즉시):

   ```bash
   aws ec2 authorize-security-group-ingress --region ap-northeast-2 --group-id sg-04abc98cb9e5262b8 \
     --ip-permissions 'IpProtocol=tcp,FromPort=80,ToPort=80,IpRanges=[{CidrIp=0.0.0.0/0}]' \
                      'IpProtocol=tcp,FromPort=443,ToPort=443,IpRanges=[{CidrIp=0.0.0.0/0}]'
   ```

스택을 삭제하면 이 스택이 추가한 Cloudflare 규칙만 사라진다. 4단계 이후에 삭제하면 사이트가 막히므로 6단계 롤백을 먼저 한다.

Cloudflare 대역이 바뀌면 https://www.cloudflare.com/ips-v4 를 다시 받아 템플릿의 `Entries`를 고치고 같은 `deploy` 명령으로 업데이트한다
(MaxEntries 20 중 15개 사용 중).
