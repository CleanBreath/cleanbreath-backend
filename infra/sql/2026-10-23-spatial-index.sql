-- 금연·흡연 구역 좌표에 공간 인덱스(R-tree) 추가 (MySQL 8.0 이상, RDS 8.4에서 확인)
-- 기존 path_lat·path_lng 문자열 컬럼은 API 응답용으로 그대로 두고, 검색용 도형 컬럼을 추가한다.
-- 좌표 순서는 POINT(경도 위도), SRID 0(평면). 8.x는 SRID 속성이 없는 컬럼의 공간 인덱스를 쓰지 않으므로 SRID 0을 명시한다.
-- 실행 후 새 백엔드(Path.area 매핑)를 바로 배포해야 한다. 옛 백엔드는 area 없이 path를 INSERT해 실패한다.

-- 1. 대표 좌표: address_pos_lat·lng에서 자동 계산되는 STORED 생성 컬럼
ALTER TABLE address
  ADD COLUMN location POINT GENERATED ALWAYS AS (POINT(address_pos_lng, address_pos_lat)) STORED NOT NULL SRID 0,
  ADD SPATIAL INDEX idx_address_location (location);

-- 2. 구역 다각형: 값을 채운 뒤 NOT NULL과 인덱스를 건다 (공간 인덱스는 NOT NULL 컬럼만 가능)
ALTER TABLE path ADD COLUMN area POLYGON SRID 0 NULL;

-- 꼭짓점이 많으면 기본 1024바이트에서 잘리므로 늘린다
SET SESSION group_concat_max_len = 1048576;

-- 끝점이 시작점과 다르면 시작점을 붙여 고리를 닫는다 (Path.toPolygonWkt와 같은 규칙)
UPDATE path p
JOIN (
  SELECT s.path_id,
         GROUP_CONCAT(CONCAT(s.lng, ' ', s.lat) ORDER BY s.i SEPARATOR ',') AS ring,
         MAX(CASE WHEN s.i = 1 THEN CONCAT(s.lng, ' ', s.lat) END) AS first_pt,
         MAX(CASE WHEN s.i = s.n THEN CONCAT(s.lng, ' ', s.lat) END) AS last_pt
  FROM (
    SELECT p2.path_id, t.i, t.lat,
           JSON_EXTRACT(CONCAT('[', p2.path_lng, ']'), CONCAT('$[', t.i - 1, ']')) AS lng,
           JSON_LENGTH(CONCAT('[', p2.path_lat, ']')) AS n
    FROM path p2,
         JSON_TABLE(CONCAT('[', p2.path_lat, ']'), '$[*]' COLUMNS (i FOR ORDINALITY, lat JSON PATH '$')) t
  ) s
  GROUP BY s.path_id
) r ON r.path_id = p.path_id
SET p.area = ST_GeomFromText(
  CONCAT('POLYGON((', r.ring, IF(r.first_pt = r.last_pt, '', CONCAT(',', r.first_pt)), '))'), 0);

ALTER TABLE path
  MODIFY area POLYGON SRID 0 NOT NULL,
  ADD SPATIAL INDEX idx_path_area (area);

-- 확인: 0, 0이어야 한다 (ST_IsValid가 0인 도형은 손으로 그린 끝점의 미세한 꼬임이며 포함 판정은 정상 동작한다)
-- SELECT COUNT(*) FROM path WHERE area IS NULL;
-- SELECT COUNT(*) FROM path WHERE ST_NumPoints(ST_ExteriorRing(area)) < 4;

-- 되돌리기
-- ALTER TABLE path DROP INDEX idx_path_area, DROP COLUMN area;
-- ALTER TABLE address DROP INDEX idx_address_location, DROP COLUMN location;
