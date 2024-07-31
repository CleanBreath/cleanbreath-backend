package cleanbreath.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ResponseAllDataUpdateCheckDTO<T> {
    private int count;
    private LocalDateTime updateDate;
    private T data;

    public ResponseAllDataUpdateCheckDTO(int count, LocalDateTime updateAt, T data) {
        this.count = count;
        this.updateDate = updateAt;
        this.data = data;
    }
}
