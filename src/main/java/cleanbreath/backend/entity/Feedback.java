package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    public void updateFeedback(LocalDateTime updateAt, String title, String content) {
        this.createAt = updateAt;
        this.title = title;
        this.content = content;
    }
}
