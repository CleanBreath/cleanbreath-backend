package cleanbreath.backend.dto.AddressDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseSaveMessage {
    private HttpStatus status;
    private String message;

    public ResponseSaveMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
