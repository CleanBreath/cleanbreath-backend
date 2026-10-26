// 공공데이터포털 전국금연구역표준데이터에서 실외 금연구역만 골라 public_no_smoking_zone 적재 SQL을 만든다.
// 사용: PUBLIC_DATA_PORTAL_API_KEY=<Decoding 키> node infra/scripts/public-no-smoking-zone.mjs 서울특별시 [경기도 ...] > out.sql
// 시도마다 DELETE 후 INSERT를 한 트랜잭션으로 묶어, 다시 돌려도 그 시도만 최신으로 바뀐다.
import { pathToFileURL } from "node:url";

const ENDPOINT = "https://api.data.go.kr/openapi/tn_pubr_public_prhsmk_zn_api";
const PAGE_SIZE = 1000;

// prhsmkSe(구분)는 기관마다 자유 서술이라 키워드로 묶는다. 순서가 중요: 앞에서 걸린 유형으로 정한다
const DIVISION_RULES = [
  ["TRANSIT", /정류|승차대|승강|택시|출입구|지하철|역$|역\s|환승|터미널/],
  ["PARK", /공원|놀이|광장|마을마당|녹지|하천|공공공지/],
  ["STREET", /거리|통학로|보행로|보도|차도|구간|\d길/],
  ["BOUNDARY", /경계|정화구역|보호구역|30m|10m|주변|근처/i],
];
// 구분이 실내 시설이어도 범위가 "건물 밖 몇 m"면 실외 구역이다
const OUTDOOR_SCOPE = /10m|10미터|30m|30미터|반경|보도|보차도|경계선/i;

/** 실외 금연구역이면 zone_type, 실내 시설이면 null */
export function classify(division = "", scope = "") {
  const hit = DIVISION_RULES.find(([, re]) => re.test(division));
  if (hit) return hit[0];
  return OUTDOOR_SCOPE.test(scope) ? "BOUNDARY" : null;
}

/** API 한 행을 테이블 행으로 바꾼다. 실내 시설이거나 좌표가 국내 범위 밖이면 null */
export function toRow(item, sido) {
  const type = classify(item.prhsmkSe, item.prhsmkScopeDesc);
  const lat = Number(item.latitude);
  const lng = Number(item.longitude);
  const name = item.prhsmkNm?.trim();
  if (!type || !name || !(lat > 33 && lat < 39 && lng > 124 && lng < 132)) return null;
  const date = /^\d{4}-\d{2}-\d{2}$/.test(item.referenceDate ?? "") ? item.referenceDate : null;
  return {
    name: name.slice(0, 255),
    zone_type: type,
    division: item.prhsmkSe?.slice(0, 255) || null,
    scope: item.prhsmkScopeDesc?.slice(0, 500) || null,
    sido,
    sigungu: item.signguNm?.slice(0, 50) || null,
    road_address: (item.rdnmadr || item.lnmadr)?.slice(0, 255) || null,
    lat,
    lng,
    reference_date: date,
  };
}

export function sqlValue(v) {
  if (v === null || v === undefined) return "NULL";
  if (typeof v === "number") return String(v);
  return `'${String(v).replace(/\0/g, "").replace(/\\/g, "\\\\").replace(/'/g, "''")}'`;
}

async function fetchPage(sido, pageNo) {
  const url = new URL(ENDPOINT);
  url.search = new URLSearchParams({
    serviceKey: process.env.PUBLIC_DATA_PORTAL_API_KEY,
    pageNo: String(pageNo),
    numOfRows: String(PAGE_SIZE),
    type: "json",
    ctprvnNm: sido,
  });
  for (let attempt = 1; ; attempt++) {
    try {
      const json = await (await fetch(url)).json();
      const body = json.response?.body ?? json.body;
      if (!body) throw new Error(JSON.stringify(json).slice(0, 300));
      return body;
    } catch (error) {
      if (attempt === 4) throw error;
      await new Promise((r) => setTimeout(r, 2000 * attempt));
    }
  }
}

async function fetchAll(sido) {
  const first = await fetchPage(sido, 1);
  const items = [...(first.items?.item ?? [])];
  const pages = Math.ceil(first.totalCount / PAGE_SIZE);
  for (let p = 2; p <= pages; p += 5) {
    const batch = await Promise.all(
      Array.from({ length: Math.min(5, pages - p + 1) }, (_, i) => fetchPage(sido, p + i)),
    );
    for (const body of batch) items.push(...(body.items?.item ?? []));
  }
  if (items.length !== first.totalCount) {
    throw new Error(`${sido}: ${first.totalCount}건 중 ${items.length}건만 받음`);
  }
  return items;
}

const COLUMNS = ["name", "zone_type", "division", "scope", "sido", "sigungu", "road_address", "lat", "lng", "reference_date"];

async function main(sidos) {
  if (!process.env.PUBLIC_DATA_PORTAL_API_KEY) throw new Error("PUBLIC_DATA_PORTAL_API_KEY가 없습니다");
  if (sidos.length === 0) throw new Error("시도명을 하나 이상 넘겨주세요 (예: 서울특별시)");
  const out = ["SET NAMES utf8mb4;"];
  for (const sido of sidos) {
    const items = await fetchAll(sido);
    const rows = items.map((item) => toRow(item, sido)).filter(Boolean);
    console.error(`${sido}: 전체 ${items.length}건 중 실외 ${rows.length}건`);
    // 지운 뒤 빈 데이터를 넣는 사고를 막는다
    if (rows.length === 0) throw new Error(`${sido}: 실외 구역이 0건이라 중단`);
    out.push("START TRANSACTION;", `DELETE FROM public_no_smoking_zone WHERE sido = ${sqlValue(sido)};`);
    for (let i = 0; i < rows.length; i += 500) {
      const values = rows
        .slice(i, i + 500)
        .map((r) => `(${COLUMNS.map((c) => sqlValue(r[c])).join(",")})`)
        .join(",\n");
      // 같은 좌표·같은 이름은 uk_public_zone_pos_name으로 하나만 남긴다
      out.push(`INSERT IGNORE INTO public_no_smoking_zone (${COLUMNS.join(",")}) VALUES\n${values};`);
    }
    out.push("COMMIT;");
  }
  process.stdout.write(`${out.join("\n")}\n`);
}

if (import.meta.url === pathToFileURL(process.argv[1]).href) {
  main(process.argv.slice(2)).catch((error) => {
    console.error(error.message);
    process.exit(1);
  });
}
