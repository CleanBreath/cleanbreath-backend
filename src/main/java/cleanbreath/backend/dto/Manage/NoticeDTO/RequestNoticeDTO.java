package cleanbreath.backend.dto.Manage.NoticeDTO;

import cleanbreath.backend.entity.manage.Notice;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RequestNoticeDTO {
    private String title;
    private String content;

    public Notice toEntity() {
        return Notice.builder()
                .title(title)
                .content(content)
                .updateAt(LocalDateTime.now())
                .build();
    }
}
