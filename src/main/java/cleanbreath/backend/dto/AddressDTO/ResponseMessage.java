package cleanbreath.backend.dto.AddressDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseMessage {
    private HttpStatus status;
    private String message;

    public ResponseMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
