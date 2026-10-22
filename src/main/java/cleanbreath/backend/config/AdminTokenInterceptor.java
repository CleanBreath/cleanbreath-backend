package cleanbreath.backend.config;

import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 관리용 API를 ADMIN_TOKEN Bearer 토큰으로 보호하는 인터셉터.
 * 토큰이 설정되지 않으면 모든 요청을 거부한다.
 */
public class AdminTokenInterceptor implements HandlerInterceptor {

    private static final String BEARER = "Bearer ";

    private final byte[] adminToken;

    public AdminTokenInterceptor(String adminToken) {
        this.adminToken = adminToken == null ? new byte[0] : adminToken.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 타이밍 공격을 막기 위해 상수 시간 비교
        if (adminToken.length == 0 || header == null || !header.startsWith(BEARER)
                || !MessageDigest.isEqual(adminToken, header.substring(BEARER.length()).getBytes(StandardCharsets.UTF_8))) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return true;
    }
}
