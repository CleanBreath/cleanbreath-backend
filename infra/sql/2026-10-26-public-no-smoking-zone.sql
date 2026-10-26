-- 공공데이터포털 전국금연구역표준데이터 중 실외 금연구역(점 좌표)을 담는 테이블 (MySQL 8.4)
-- 기존 address·path(직접 그린 다각형)와 출처·형태가 달라 별도 테이블로 둔다.
-- 데이터는 infra/scripts/public-no-smoking-zone.mjs가 만든 SQL로 시도 단위로 통째로 갈아 끼운다.
-- 백엔드(PublicNoSmokingZone 엔티티, ddl-auto: validate)를 배포하기 전에 먼저 실행해야 한다.

CREATE TABLE public_no_smoking_zone (
  zone_id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(255) NOT NULL,
  zone_type      VARCHAR(20)  NOT NULL COMMENT 'TRANSIT | PARK | STREET | BOUNDARY',
  division       VARCHAR(255) NULL     COMMENT '원본 prhsmkSe(구분)',
  scope          VARCHAR(500) NULL     COMMENT '원본 prhsmkScopeDesc(금연구역 범위)',
  sido           VARCHAR(20)  NOT NULL,
  sigungu        VARCHAR(50)  NULL,
  road_address   VARCHAR(255) NULL,
  lat            DOUBLE       NOT NULL,
  lng            DOUBLE       NOT NULL,
  reference_date DATE         NULL,
  -- 좌표 순서는 POINT(경도 위도), SRID 0. idx_address_location과 같은 규칙
  location POINT GENERATED ALWAYS AS (POINT(lng, lat)) STORED NOT NULL SRID 0,
  UNIQUE KEY uk_public_zone_pos_name (lat, lng, name),
  KEY idx_public_zone_sido (sido),
  SPATIAL INDEX idx_public_zone_location (location)
) DEFAULT CHARSET = utf8mb4;

-- 확인
-- SELECT sido, zone_type, COUNT(*) FROM public_no_smoking_zone GROUP BY sido, zone_type;

-- 되돌리기
-- DROP TABLE public_no_smoking_zone;
