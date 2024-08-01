package cleanbreath.backend.dto.FeedbackDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestFeedbackAccountDTO {
    private Long id;
    private String username;
    private String password;
}
