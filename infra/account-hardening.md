# 계정 하드닝 (CloudFormation 밖에서 CLI로 적용)

대상: 계정 `575108947764`, 리전 `ap-northeast-2`. 아래 항목은 CloudFormation 리소스 타입이 없거나(2026-10 CloudFormation 리소스 목록 확인)
CloudFormation이 만들지 않은 기존 인스턴스를 바꾸는 작업이라 CLI로 적용한다. 항목마다 **적용 → 확인 → 롤백** 순서로 적었다.

| 항목 | CloudFormation 지원 | 적용 위치 |
| --- | --- | --- |
| EBS 스냅샷 퍼블릭 액세스 차단 | 지원 (`AWS::EC2::SnapshotBlockPublicAccess`) | `security-baseline.yml` |
| EBS 기본 암호화 | 미지원 | 아래 1 |
| 계정 단위 S3 퍼블릭 액세스 차단 | 미지원 (`AWS::S3::*`에 계정 단위 리소스 없음) | 아래 2 |
| IAM 비밀번호 정책 | 미지원 | 아래 3 |
| IMDS hop limit 1 | 기존 인스턴스 수정이라 해당 없음 | 아래 4 |

모든 명령은 관리자 자격 증명으로 직접 실행한다. 순서는 상관없고, 각 항목은 서로 독립적이다.

## 1. EBS 기본 암호화

리전 단위 설정이다. 켠 뒤 **새로 만드는 볼륨과 스냅샷 복사본만** `aws/ebs` 키로 암호화된다. 지금 붙어 있는 루트 볼륨은 그대로 남는다
(기존 볼륨 암호화는 스냅샷 → 암호화 복사 → 볼륨 교체가 필요하고 다운타임이 생기므로 별도 작업으로 둔다).

```bash
# 적용
aws ec2 enable-ebs-encryption-by-default --region ap-northeast-2
# 확인: "EbsEncryptionByDefault": true
aws ec2 get-ebs-encryption-by-default --region ap-northeast-2
# 롤백
aws ec2 disable-ebs-encryption-by-default --region ap-northeast-2
```

다른 리전에서도 EC2를 쓰게 되면 그 리전에서 같은 명령을 한 번 더 실행한다.

## 2. 계정 단위 S3 퍼블릭 액세스 차단

2026-10 기준 버킷 4개(`cdk-hnb659fds-assets-...`, `cmu02-portpolio`, `phantom-files-prod`, `phantom-files-prod-access-logs`)가
모두 이미 버킷 단위 차단 4개 항목이 켜져 있어서, 계정 단위로 켜도 지금 동작이 바뀌는 버킷은 없다. 앞으로 실수로 공개 버킷을 만드는 것을 막는 용도다.

```bash
# 적용
aws s3control put-public-access-block --account-id 575108947764 \
  --public-access-block-configuration BlockPublicAcls=true,IgnorePublicAcls=true,BlockPublicPolicy=true,RestrictPublicBuckets=true
# 확인: 네 값이 모두 true
aws s3control get-public-access-block --account-id 575108947764
# 롤백
aws s3control delete-public-access-block --account-id 575108947764
```

## 3. IAM 비밀번호 정책

콘솔 비밀번호가 있는 IAM 사용자는 `hyeonjun` 하나다(`picktrip-dev`는 콘솔 비밀번호 없음). 정책은 **다음 비밀번호 변경부터** 적용되고,
현재 비밀번호를 즉시 무효화하지 않는다. 주기적 강제 변경(`--max-password-age`)은 NIST 800-63B 권고에 따라 넣지 않았다.

```bash
# 적용
aws iam update-account-password-policy \
  --minimum-password-length 14 \
  --require-uppercase-characters --require-lowercase-characters \
  --require-numbers --require-symbols \
  --password-reuse-prevention 24 \
  --allow-users-to-change-password
# 확인
aws iam get-account-password-policy
# 롤백 (정책 삭제 = AWS 기본값으로 돌아감)
aws iam delete-account-password-policy
```

## 4. IMDS hop limit 1 (`i-0acb8d4abc94e558b`)

현재 IMDSv2 필수 + hop limit 2다. hop limit을 1로 낮추면 Docker 브리지 네트워크의 컨테이너는 IMDS에 닿지 못하고,
호스트(SSM 에이전트 등)는 그대로 쓸 수 있다. 재부팅이 필요 없다.

적용 전 확인할 것: CleanBreath 백엔드 코드에는 AWS SDK와 IMDS 호출이 없다. **같은 인스턴스의 PickTrip 컨테이너가 인스턴스 프로파일
자격 증명(IMDS)을 쓰는지는 확인하지 못했으니** PickTrip 담당자에게 먼저 확인한다.

```bash
# 적용
aws ec2 modify-instance-metadata-options --region ap-northeast-2 \
  --instance-id i-0acb8d4abc94e558b \
  --http-tokens required --http-endpoint enabled --http-put-response-hop-limit 1
# 확인: HttpPutResponseHopLimit 1, HttpTokens required
aws ec2 describe-instances --region ap-northeast-2 --instance-ids i-0acb8d4abc94e558b \
  --query 'Reservations[0].Instances[0].MetadataOptions'
# 동작 확인 (인스턴스에서): 호스트는 토큰을 받고, 컨테이너 안에서는 타임아웃이 나야 정상
curl -s -m 3 -X PUT http://169.254.169.254/latest/api/token -H 'X-aws-ec2-metadata-token-ttl-seconds: 60'; echo
docker run --rm curlimages/curl -s -m 3 -X PUT http://169.254.169.254/latest/api/token -H 'X-aws-ec2-metadata-token-ttl-seconds: 60'; echo "exit=$?"
# 롤백
aws ec2 modify-instance-metadata-options --region ap-northeast-2 \
  --instance-id i-0acb8d4abc94e558b --http-put-response-hop-limit 2
```

## 참고: 템플릿 밖으로 남겨 둔 것

- `picktrip-db` RDS가 `PubliclyAccessible=true`다. PickTrip과 공유하는 DB라 이 작업 범위에서 바꾸지 않았다.
  최소한 DB 보안 그룹의 3306 인바운드가 EC2 보안 그룹(또는 관리자 IP)으로만 열려 있는지 확인하고, 가능하면
  `aws rds modify-db-instance --db-instance-identifier picktrip-db --no-publicly-accessible --apply-immediately`로 닫는다
  (외부에서 직접 붙는 클라이언트가 있으면 끊기므로 PickTrip 담당자와 일정을 맞출 것).
  `security-baseline.yml`의 GuardDuty RDS 로그인 모니터링이 그동안의 무차별 대입 시도를 탐지한다.
- AMI 퍼블릭 공유 차단은 이미 `block-new-sharing` 상태라 따로 손대지 않았다.
