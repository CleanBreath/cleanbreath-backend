// node --test infra/scripts/public-no-smoking-zone.test.mjs
import assert from "node:assert/strict";
import { test } from "node:test";
import { classify, sqlValue, toRow } from "./public-no-smoking-zone.mjs";

test("classify: 실외 구분은 유형으로, 실내 시설은 null", () => {
  assert.equal(classify("가로변버스정류소"), "TRANSIT");
  assert.equal(classify("어린이놀이시설"), "PARK");
  assert.equal(classify("신길로 28길"), "STREET");
  assert.equal(classify("어린이집 시설경계"), "BOUNDARY");
  assert.equal(classify("일반음식점", "시설전체"), null);
  assert.equal(classify("신길5동새마을금고"), null);
  // 구분은 실내여도 범위가 건물 밖이면 실외
  assert.equal(classify("연면적 1000㎡이상의 사무용 건축물", "건물경계선으로부터 30m이내 보차도"), "BOUNDARY");
});

test("toRow: 좌표가 없거나 국내 범위 밖이면 버린다", () => {
  const base = { prhsmkNm: "정류소", prhsmkSe: "버스정류소", latitude: "37.5", longitude: "127.0", referenceDate: "2025-02-10" };
  assert.equal(toRow(base, "서울특별시").zone_type, "TRANSIT");
  assert.equal(toRow({ ...base, latitude: "" }, "서울특별시"), null);
  assert.equal(toRow({ ...base, longitude: "0" }, "서울특별시"), null);
  assert.equal(toRow({ ...base, referenceDate: "" }, "서울특별시").reference_date, null);
});

test("sqlValue: 따옴표·역슬래시를 이스케이프한다", () => {
  assert.equal(sqlValue(null), "NULL");
  assert.equal(sqlValue(37.5), "37.5");
  assert.equal(sqlValue("O'Neil\\"), "'O''Neil\\\\'");
});
