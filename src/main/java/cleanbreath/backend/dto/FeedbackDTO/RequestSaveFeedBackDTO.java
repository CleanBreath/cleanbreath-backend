package cleanbreath.backend.dto.FeedbackDTO;

import cleanbreath.backend.entity.Feedback;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestSaveFeedBackDTO {
    private String username;
    private String password;
    private String title;
    private String content;

    public Feedback toEntity() {
        return Feedback.builder()
                .username(username)
                .password(password)
                .createAt(LocalDateTime.now())
                .title(title)
                .content(content)
                .build();
    }
}
