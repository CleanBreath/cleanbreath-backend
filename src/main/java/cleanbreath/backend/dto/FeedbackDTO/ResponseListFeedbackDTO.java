package cleanbreath.backend.dto.FeedbackDTO;

import cleanbreath.backend.entity.Feedback;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseListFeedbackDTO {
    private Long feedback_id;
    private LocalDateTime createAt;
    private String title;
    private String username;

    public ResponseListFeedbackDTO(Feedback feedback) {
        this.feedback_id = feedback.getId();
        this.createAt = feedback.getCreateAt();
        this.title = feedback.getTitle();
        this.username = feedback.getUsername();
    }
}
