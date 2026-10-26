package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "feedback")
public class Feedback {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fd_id")
    private Long id;

    @Column(name = "fd_create_at")
    private LocalDateTime createAt;

    @Column(name = "fd_title")
    private String title;

    @Column(name = "fd_content", columnDefinition = "MEDIUMTEXT")
    private String content;

    // createAt은 1년 보관 기간의 기준이라 수정해도 바꾸지 않는다
    public void updateFeedback(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
