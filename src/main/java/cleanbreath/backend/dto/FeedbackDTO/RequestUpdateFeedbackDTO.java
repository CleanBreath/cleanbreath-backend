package cleanbreath.backend.dto.FeedbackDTO;

import cleanbreath.backend.entity.Feedback;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RequestUpdateFeedbackDTO {
    private LocalDateTime updateAt;
    private String title;
    private String content;
}
