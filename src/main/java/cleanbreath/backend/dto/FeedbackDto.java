package cleanbreath.backend.dto;

import cleanbreath.backend.entity.Feedback;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class FeedbackDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Create {

        @NotBlank @Size(max = 100)
        private String title;
        @NotBlank @Size(max = 2000)
        private String content;

        public Feedback toEntity() {
            return Feedback.builder()
                    .createAt(LocalDateTime.now())
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Update {
        // 예전 클라이언트 호환용으로만 받고 쓰지 않는다 (createAt은 보관 기간 기준이라 수정하지 않음)
        private LocalDateTime updateAt;
        @NotBlank @Size(max = 100)
        private String title;
        @NotBlank @Size(max = 2000)
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Account {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private LocalDateTime createAt;
        private String title;
        private String content;

        public Response(Feedback feedback) {
            this.id = feedback.getId();
            this.createAt = feedback.getCreateAt();
            this.title = feedback.getTitle();
            this.content = feedback.getContent();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ListResponse {
        private Long feedbackId;
        private LocalDateTime createAt;
        private String title;

        public ListResponse(Feedback feedback) {
            this.feedbackId = feedback.getId();
            this.createAt = feedback.getCreateAt();
            this.title = feedback.getTitle();
        }
    }
}
