package cleanbreath.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 주소(Address) 관련
    ADDRESS_NOT_FOUND("해당 주소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PENDING_ADDRESS_NOT_FOUND("해당 요청 주소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PENDING_PATH_NOT_FOUND("해당 요청 경로를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 아파트(Apartment) 관련
    REGION_NOT_FOUND("해당 지역은 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 피드백(Feedback) 관련
    FEEDBACK_NOT_FOUND("해당 피드백이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    FEEDBACK_INVALID("피드백 제목 또는 내용이 비어있습니다.", HttpStatus.BAD_REQUEST),

    // 공통
    INVALID_INPUT("잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_VALIDATION_REQUEST("이미 해당 대상에 대한 검증 요청이 존재합니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;
}
