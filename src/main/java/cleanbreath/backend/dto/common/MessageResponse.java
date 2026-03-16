package cleanbreath.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponse implements BaseResponse {
    private String message;

    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }
}
