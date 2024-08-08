package cleanbreath.backend.dto.Manage.NoticeDTO;

import cleanbreath.backend.entity.manage.Notice;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseNoticeDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime updateAt;

    public ResponseNoticeDTO(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.updateAt = notice.getUpdateAt();
    }
}
