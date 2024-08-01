package cleanbreath.backend.dto.FeedbackDTO;

import cleanbreath.backend.entity.Feedback;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseFeedbackDTO {
    private Long id;
    private String username;
    private LocalDateTime createAt;
    private String title;
    private String content;

    public ResponseFeedbackDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.username = feedback.getUsername();
        this.createAt = feedback.getCreateAt();
        this.title = feedback.getTitle();
        this.content = feedback.getContent();
    }
}
