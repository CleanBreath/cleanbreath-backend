package cleanbreath.backend.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Path.toPolygonWkt 변환 규칙 테스트. infra/sql/2026-10-23-spatial-index.sql의 백필과 같은 규칙이어야 한다.
 */
class PathTest {

  @Test
  @DisplayName("열린 고리는 시작점을 붙여 닫고, 좌표는 (경도 위도) 순서로 쓴다")
  void openRing_isClosed() {
    String wkt = Path.toPolygonWkt("37.1,37.2,37.3", "126.1,126.2,126.3");

    assertThat(wkt).isEqualTo("POLYGON((126.1 37.1,126.2 37.2,126.3 37.3,126.1 37.1))");
  }

  @Test
  @DisplayName("이미 닫힌 고리는 그대로 두고 공백은 무시한다")
  void closedRing_isKept() {
    String wkt = Path.toPolygonWkt("37.1, 37.2, 37.3, 37.1", "126.1, 126.2, 126.3, 126.1");

    assertThat(wkt).isEqualTo("POLYGON((126.1 37.1,126.2 37.2,126.3 37.3,126.1 37.1))");
  }

  @Test
  @DisplayName("작은 값도 지수 표기 없이 쓴다")
  void smallNumbers_plainNotation() {
    String wkt = Path.toPolygonWkt("0.0001,0.0002,0.0003", "0.0001,0.0002,0.0003");

    assertThat(wkt).doesNotContain("E");
  }

  @Test
  @DisplayName("끝에 겹쳐 찍힌 점과 시작점 바로 옆의 닫는 클릭을 지운다 (운영 path 4의 실제 좌표)")
  void nearDuplicateTail_isRemoved() {
    Path.Coordinates cleaned = Path.removeNearDuplicatePoints(
        "37.40015614313889,37.39986535945584,37.399764067768196,37.40007511919417,37.400153887349575,37.400153887349575",
        "126.93221376743796,126.93185827163431,126.93198541950272,126.9323324266534,126.93220812250028,126.93220812250028");

    assertThat(cleaned.pathLat()).isEqualTo("37.40015614313889,37.39986535945584,37.399764067768196,37.40007511919417");
    assertThat(cleaned.pathLng()).isEqualTo("126.93221376743796,126.93185827163431,126.93198541950272,126.9323324266534");
  }

  @Test
  @DisplayName("중간에 0.25m 간격으로 붙은 점을 지운다 (운영 path 58의 실제 좌표)")
  void nearDuplicateMiddle_isRemoved() {
    Path.Coordinates cleaned = Path.removeNearDuplicatePoints(
        "37.38123158089662,37.38085097187389,37.38085097077727,37.38115767717581,37.381522510035516",
        "126.95386737801618,126.95405673616432,126.95405391340378,126.95498524127483,126.95477049143811");

    assertThat(cleaned.pathLat()).isEqualTo("37.38123158089662,37.38085097187389,37.38115767717581,37.381522510035516");
  }

  @Test
  @DisplayName("겹친 점이 없으면 그대로 두고, 꼭짓점은 3개 아래로 줄이지 않는다")
  void cleanShape_isKept() {
    assertThat(Path.removeNearDuplicatePoints("37.1,37.2,37.3", "126.1,126.2,126.3"))
        .isEqualTo(new Path.Coordinates("37.1,37.2,37.3", "126.1,126.2,126.3"));
    assertThat(Path.removeNearDuplicatePoints("37.4,37.4001,37.40001", "127.0,127.0001,127.00001").pathLat())
        .isEqualTo("37.4,37.4001,37.40001");
  }

  @Test
  @DisplayName("개수가 다르거나, 꼭짓점이 3개 미만이거나, 숫자가 아니거나, 범위를 벗어나면 거부한다")
  void invalidInput_throws() {
    assertThatThrownBy(() -> Path.toPolygonWkt("37.1,37.2,37.3", "126.1,126.2"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Path.toPolygonWkt("37.1,37.2", "126.1,126.2"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Path.toPolygonWkt("37.1,abc,37.3", "126.1,126.2,126.3"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Path.toPolygonWkt("37.1,NaN,37.3", "126.1,126.2,126.3"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Path.toPolygonWkt("97.1,37.2,37.3", "126.1,126.2,126.3"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Path.toPolygonWkt(null, "126.1,126.2,126.3"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
