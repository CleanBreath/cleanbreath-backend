package cleanbreath.backend.dto.AddressDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseSaveAddressDTO {
    private HttpStatus status;
    private String message;

    public ResponseSaveAddressDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
