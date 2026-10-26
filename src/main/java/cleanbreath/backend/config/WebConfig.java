package cleanbreath.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    // 운영 기본값은 서비스 도메인만 허용, 로컬 개발은 .env의 CORS_ALLOWED_ORIGINS로 추가
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    // 관리용 API 토큰, 미설정 시 관리용 API 전부 401
    @Value("${admin.token:}")
    private String adminToken;

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // 피드백 조회·수정·삭제와 /v1/admin/** 는 관리자만, 피드백 등록은 공개
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminTokenInterceptor(adminToken))
                .addPathPatterns("/v1/feedback-list", "/v1/feedback/*", "/v1/admin/**")
                .excludePathPatterns("/v1/feedback/add");
    }
}
