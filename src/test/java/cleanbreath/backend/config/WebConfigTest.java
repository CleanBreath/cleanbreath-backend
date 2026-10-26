package cleanbreath.backend.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * WebConfig CORS 허용 출처 단위 테스트.
 */
class WebConfigTest {

  private static final String PROD = "https://cleanbreath.cmu02-studio.com";
  private static final String LOCAL = "http://localhost:3000";

  // CorsRegistry의 protected 설정 조회를 테스트에서 열어줌
  static class ExposedCorsRegistry extends CorsRegistry {
    Map<String, CorsConfiguration> configs() {
      return getCorsConfigurations();
    }
  }

  private CorsConfiguration corsFor(String... origins) {
    WebConfig config = new WebConfig();
    ReflectionTestUtils.setField(config, "allowedOrigins", origins);
    ExposedCorsRegistry registry = new ExposedCorsRegistry();
    config.addCorsMappings(registry);
    return registry.configs().get("/**");
  }

  @Test
  @DisplayName("운영 기본값에서는 서비스 도메인만 허용하고 localhost는 거부한다")
  void prodDefault_rejectsLocalhost() {
    CorsConfiguration cors = corsFor(PROD);

    assertThat(cors.checkOrigin(PROD)).isEqualTo(PROD);
    assertThat(cors.checkOrigin(LOCAL)).isNull();
  }

  @Test
  @DisplayName("설정에 추가한 출처는 허용한다")
  void configuredOrigins_areAllowed() {
    CorsConfiguration cors = corsFor(LOCAL, PROD);

    assertThat(cors.checkOrigin(LOCAL)).isEqualTo(LOCAL);
    assertThat(cors.checkOrigin("https://evil.example")).isNull();
  }

  @Test
  @DisplayName("Bearer 헤더 인증이라 credentials는 허용하지 않고, 쓰지 않는 PATCH는 막는다")
  void noCredentials_noPatch() {
    CorsConfiguration cors = corsFor(PROD);

    assertThat(cors.getAllowCredentials()).isFalse();
    assertThat(cors.getAllowedMethods()).containsExactly("GET", "POST", "PUT", "DELETE");
  }
}
