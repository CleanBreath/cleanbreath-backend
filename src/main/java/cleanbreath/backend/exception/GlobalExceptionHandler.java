package cleanbreath.backend.exception;


import cleanbreath.backend.dto.common.MessageResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    // 클라이언트 요청 오류는 500이 아닌 4xx로 응답하고, 상세 원인은 로그로만 남김
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            ConstraintViolationException.class,
            NoResourceFoundException.class,
            NoHandlerFoundException.class
    })
    public ResponseEntity<MessageResponse> handleClientError(Exception e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        // ErrorResponse 구현 예외는 Spring이 정한 상태 코드와 헤더(405의 Allow, 415의 Accept)를 그대로 사용
        ResponseEntity.BodyBuilder response = e instanceof ErrorResponse er
                ? ResponseEntity.status(er.getStatusCode()).headers(er.getHeaders())
                : ResponseEntity.badRequest();

        String message = switch (e) {
            case HttpRequestMethodNotSupportedException ignored -> "지원하지 않는 요청 메서드입니다.";
            case HttpMediaTypeNotSupportedException ignored -> "지원하지 않는 요청 형식입니다.";
            case NoResourceFoundException ignored -> "요청한 경로를 찾을 수 없습니다.";
            case NoHandlerFoundException ignored -> "요청한 경로를 찾을 수 없습니다.";
            default -> ErrorCode.INVALID_INPUT.getMessage();
        };

        return response.body(MessageResponse.of(message));
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
