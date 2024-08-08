package cleanbreath.backend.entity.manage;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Long id;

    @Column(name = "n_update_at")
    private LocalDateTime updateAt;

    @Column(name = "n_title")
    private String title;

    @Column(name = "n_content")
    private String content;

    public void updateNotice(String title, String content) {
        this.updateAt = LocalDateTime.now();
        this.title = title;
        this.content = content;
    }
}
