# CleanBreath Backend API

## 📋 서비스 소개

CleanBreath는 안양시의 금연구역 및 흡연구역 정보를 제공하는 서비스입니다. 사용자들이 금연구역과 흡연 가능 구역을 쉽게 찾을 수 있도록 위치 기반 정보를 제공하며, 공동주택 금연구역 지정 현황도 함께 제공합니다.

### 주요 목적

- 금연구역 및 흡연구역의 정확한 위치 정보 제공
- 안양시 공동주택 금연구역 지정 현황 조회
- 사용자 피드백을 통한 서비스 개선
- 신규 흡연구역 제안 및 관리

## 🚀 핵심 기능

### 1. 금연/흡연구역 관리

- 전체 금연 및 흡연구역 정보 조회
- 위도/경도 기반 특정 구역 검색
- 구역별 상세 경로(Path) 정보 제공
- 데이터 업데이트 주기 관리 (30일 기준)

### 2. 공동주택 금연구역 정보

- 안양시 전체 공동주택 금연구역 조회
- 지역별(동안구, 만안구) 필터링 검색
- 금연구역 지정 현황 (복도, 계단, 엘리베이터, 지하주차장)

### 3. 사용자 피드백 시스템

- 피드백 등록 및 조회
- 피드백 수정 및 삭제
- 관리자용 피드백 목록 관리

### 4. 신규 구역 제안

- 사용자가 새로운 흡연구역 제안
- 제안된 구역 관리 및 검토
- 페이징 처리를 통한 효율적인 데이터 조회

## 🛠 기술 스택

- **Framework**: Spring Boot 4.0.0
- **Language**: Java 21
- **Database**: MySQL
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Gradle
- **Libraries**:
  - Lombok
  - Spring Validation
  - MySQL Connector

## 📦 프로젝트 구조

```
src/main/java/cleanbreath/backend/
├── config/          # 설정 파일 (CORS, Web 설정)
├── controller/      # REST API 컨트롤러
├── dto/            # 데이터 전송 객체
│   ├── common/     # 공통 응답 DTO
│   ├── AddressDto.java
│   ├── ApartmentDto.java
│   ├── FeedbackDto.java
│   └── PendingDto.java
├── entity/         # JPA 엔티티
├── repository/     # 데이터 접근 계층
├── service/        # 비즈니스 로직
└── util/           # 유틸리티 클래스
```

## 🔧 환경 설정

### 필수 환경 변수 (.env)

```properties
DB_HOSTNAME=your-database-host
DEFAULT_SCHEMA=your-database-name
DB_USERNAME=your-username
DB_PASSWORD=your-password
```

### 애플리케이션 실행

#### 로컬 실행

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun

# 또는 JAR 파일 실행
java -jar build/libs/cleanbreath-backend-0.0.1-SNAPSHOT.jar
```

#### Docker로 실행

```bash
# Docker 이미지 빌드
docker build -t cleanbreath-backend .

# Docker 컨테이너 실행
docker run -p 8080:8080 \
  -e DB_HOSTNAME=your-db-host \
  -e DEFAULT_SCHEMA=your-db-name \
  -e DB_USERNAME=your-username \
  -e DB_PASSWORD=your-password \
  cleanbreath-backend
```

#### Docker Compose로 실행

```bash
# 전체 스택 실행 (애플리케이션 + MySQL)
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 중지
docker-compose down
```

#### GitHub Container Registry에서 이미지 가져오기

```bash
# 이미지 pull
docker pull ghcr.io/your-username/cleanbreath-backend:latest

# 실행
docker run -p 8080:8080 --env-file .env ghcr.io/your-username/cleanbreath-backend:latest
```

서버는 기본적으로 `http://localhost:8080`에서 실행됩니다.

## 🚢 CI/CD

### GitHub Actions Workflows

프로젝트는 두 가지 Docker 이미지 빌드 워크플로우를 제공합니다:

#### 1. GitHub Container Registry (GHCR)

- **파일**: `.github/workflows/docker-build.yml`
- **트리거**: `main`, `develop` 브랜치 push, PR, 태그 push
- **자동 실행**: GitHub Token 사용 (별도 설정 불필요)
- **이미지 위치**: `ghcr.io/your-username/cleanbreath-backend`

#### 2. Docker Hub

- **파일**: `.github/workflows/docker-hub.yml`
- **트리거**: `main` 브랜치 push, 태그 push, 수동 실행
- **필수 Secrets**:
  - `DOCKER_USERNAME`: Docker Hub 사용자명
  - `DOCKER_PASSWORD`: Docker Hub 액세스 토큰
- **이미지 위치**: `cleanbreath/backend`

### GitHub Secrets 설정

Docker Hub 워크플로우를 사용하려면 다음 Secrets를 설정하세요:

1. GitHub 저장소 → Settings → Secrets and variables → Actions
2. New repository secret 클릭
3. 다음 Secrets 추가:
   - `DOCKER_USERNAME`: Docker Hub 사용자명
   - `DOCKER_PASSWORD`: Docker Hub 액세스 토큰

### 태그 기반 배포

버전 태그를 푸시하면 자동으로 해당 버전의 이미지가 생성됩니다:

```bash
# 버전 태그 생성 및 푸시
git tag v1.0.0
git push origin v1.0.0
```

생성되는 이미지 태그:

- `v1.0.0`
- `1.0`
- `1`
- `latest` (main 브랜치인 경우)

## 📚 API 문서

### Base URL

```
http://localhost:8080/v1
```

---

### 🏢 Address API (금연/흡연구역)

#### 1. 전체 구역 조회

```http
GET /v1/allAddress
```

**Response**

```json
{
  "count": 100,
  "updateAt": "2024-01-01T12:00:00",
  "data": [
    {
      "id": 1,
      "addressName": "안양역 광장",
      "buildingName": "안양역",
      "latitude": 37.401234,
      "longitude": 126.921234,
      "category": "NON_SMOKING",
      "path": [
        {
          "divisionArea": "AREA_A",
          "pathsLatitude": "37.401234",
          "pathsLongitude": "126.921234"
        }
      ]
    }
  ]
}
```

#### 2. 좌표로 구역 검색

```http
GET /v1/address?lat={latitude}&lng={longitude}
```

**Parameters**

- `lat` (required): 위도
- `lng` (required): 경도

**Response**

```json
{
  "id": 1,
  "addressName": "안양역 광장",
  "buildingName": "안양역",
  "latitude": 37.401234,
  "longitude": 126.921234,
  "category": "NON_SMOKING",
  "path": [
    {
      "divisionArea": "AREA_A",
      "pathsLatitude": "37.401234",
      "pathsLongitude": "126.921234"
    }
  ]
}
```

#### 3. 데이터 업데이트 확인

```http
POST /v1/updateDate
```

**Request Body**

```json
{
  "updateDate": "2024-01-01T12:00:00.000000"
}
```

**Response (업데이트 필요시)**

```json
{
  "count": 100,
  "updateAt": "2024-01-31T12:00:00",
  "data": [...]
}
```

**Response (업데이트 불필요시)**

```json
{
  "message": "아직 업데이트 시기가 아닙니다."
}
```

---

### 🏘 Apartment API (공동주택 금연구역)

#### 1. 전체 공동주택 조회

```http
GET /v1/apartment
```

**Response**

```json
[
  {
    "id": 1,
    "region": "동안구",
    "designationNumber": "2024-001",
    "apartmentName": "평촌 아파트",
    "address": "경기도 안양시 동안구 평촌동",
    "numberOfBuilding": 10,
    "numberOfHouseholds": 500,
    "designationDate": "2024-01-01",
    "path": [
      {
        "hallway": "DESIGNATED",
        "stairs": "DESIGNATED",
        "elevator": "DESIGNATED",
        "undergroundParkingLot": "NOT_DESIGNATED",
        "latitude": 37.401234,
        "longitude": 126.921234,
        "pathsLat": "37.401234",
        "pathsLng": "126.921234"
      }
    ]
  }
]
```

#### 2. 지역별 공동주택 조회

```http
GET /v1/region?r={region}
```

**Parameters**

- `r` (required): 지역명 (예: "동안구", "만안구")

**Response**: 전체 공동주택 조회와 동일

---

### 💬 Feedback API (피드백)

#### 1. 피드백 목록 조회

```http
GET /v1/feedback-list
```

**Response**

```json
[
  {
    "feedbackId": 1,
    "createAt": "2024-01-01T12:00:00",
    "title": "서비스 개선 제안"
  }
]
```

#### 2. 피드백 등록

```http
POST /v1/feedback/add
```

**Request Body**

```json
{
  "title": "서비스 개선 제안",
  "content": "지도 UI를 개선해주세요."
}
```

**Response**

```json
{
  "message": "피드백 저장 성공"
}
```

#### 3. 피드백 상세 조회

```http
GET /v1/feedback/{id}
```

**Response**

```json
{
  "id": 1,
  "createAt": "2024-01-01T12:00:00",
  "title": "서비스 개선 제안",
  "content": "지도 UI를 개선해주세요."
}
```

#### 4. 피드백 수정

```http
PUT /v1/feedback/{id}
```

**Request Body**

```json
{
  "updateAt": "2024-01-02T12:00:00",
  "title": "수정된 제목",
  "content": "수정된 내용"
}
```

**Response**

```json
{
  "message": "업데이트 성공"
}
```

#### 5. 피드백 삭제

```http
DELETE /v1/feedback/{id}
```

**Response**

```json
{
  "message": "피드백 삭제 완료"
}
```

---

### 📍 Pending Address API (신규 구역 제안)

#### 1. 제안된 구역 전체 조회

```http
GET /v1/allRequestAddress
```

**Response**

```json
[
  {
    "id": 1,
    "addressName": "평촌역 광장",
    "buildingName": "평촌역",
    "latitude": 37.401234,
    "longitude": 126.921234,
    "category": "SMOKING",
    "paths": [
      {
        "divisionArea": "AREA_A",
        "pathLat": "37.401234",
        "pathLng": "126.921234"
      }
    ]
  }
]
```

#### 2. 제안된 구역 페이징 조회

```http
GET /v1/allRequestAddressPage?page={page}&size={size}
```

**Parameters**

- `page` (optional): 페이지 번호 (기본값: 0)
- `size` (optional): 페이지 크기 (기본값: 20, 최대: 2000)

**Response**

```json
{
  "content": [...],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

#### 3. 신규 흡연구역 제안

```http
POST /v1/smokingArea/add
```

**Request Body**

```json
{
  "updateAt": "2024-01-01 12:00:00",
  "addressName": "평촌역 광장",
  "buildingName": "평촌역",
  "latitude": 37.401234,
  "longitude": 126.921234,
  "category": "SMOKING",
  "paths": [
    {
      "divisionArea": "AREA_A",
      "pathLat": "37.401234",
      "pathLng": "126.921234"
    }
  ]
}
```

**Response**

```json
{
  "message": "주소 및 영역 저장 성공"
}
```

---

## 📊 공통 응답 형식

### ApiResponse (목록 조회)

```json
{
  "count": 100,
  "updateAt": "2024-01-01T12:00:00",
  "data": [...]
}
```

### MessageResponse (작업 결과)

```json
{
  "message": "작업 성공"
}
```

---

## 🔐 에러 응답

### 400 Bad Request

```json
{
  "message": "잘못된 요청입니다."
}
```

### 404 Not Found

```json
{
  "message": "해당 리소스를 찾을 수 없습니다."
}
```

### 500 Internal Server Error

```json
{
  "message": "서버 내부 오류가 발생했습니다."
}
```

---

## 📝 참고사항

### Category 타입

- `NON_SMOKING`: 금연구역
- `SMOKING`: 흡연구역

### DivisionArea 타입

- `AREA_A`, `AREA_B`, `AREA_C` 등 구역 구분

### NonSmokingStatus 타입 (공동주택)

- `DESIGNATED`: 지정됨
- `NOT_DESIGNATED`: 지정되지 않음

---

## 👥 기여

프로젝트에 기여하고 싶으시다면 Pull Request를 보내주세요.

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 있습니다.
