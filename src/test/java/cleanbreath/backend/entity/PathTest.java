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
