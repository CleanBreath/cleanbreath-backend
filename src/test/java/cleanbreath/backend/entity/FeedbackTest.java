package cleanbreath.backend.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FeedbackTest {

  @Test
  @DisplayName("피드백을 수정해도 보관 기간 기준인 createAt은 바뀌지 않는다")
  void update_keepsCreateAt() {
    LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 0, 0);
    Feedback feedback = Feedback.builder().createAt(createdAt).title("old").content("old").build();

    feedback.updateFeedback("new", "new content");

    assertThat(feedback.getCreateAt()).isEqualTo(createdAt);
    assertThat(feedback.getTitle()).isEqualTo("new");
    assertThat(feedback.getContent()).isEqualTo("new content");
  }
}
