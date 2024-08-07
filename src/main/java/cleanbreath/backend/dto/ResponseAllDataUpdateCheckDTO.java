package cleanbreath.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseAllDataUpdateCheckDTO<T> {
    private int count;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updateDate;
    private T data;

    public ResponseAllDataUpdateCheckDTO(int count, LocalDateTime updateAt, T data) {
        this.count = count;
        this.updateDate = updateAt;
        this.data = data;
    }
}
