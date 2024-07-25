package cleanbreath.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAllDataDTO<T> {
    private int count;
    private T data;

    public ResponseAllDataDTO(int count, T data) {
        this.count = count;
        this.data = data;
    }
}
