package cleanbreath.backend.exception;


import cleanbreath.backend.dto.common.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MessageResponse> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(MessageResponse.of(e.getMessage()));
    }

    // 예상치 못한 서버 오류는 상세 내용을 노출하지 않고 로그로만 기록
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception e) {
        log.error("Unhandled exception: ", e);
        return ResponseEntity
                .internalServerError()
                .body(MessageResponse.of("서버 내부 오류가 발생했습니다."));
    }
}
