package cleanbreath.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int count;
    private LocalDateTime updateAt;
    private T data;

    public static <T> ApiResponse<T> of(int count, LocalDateTime updateAt, T data) {
        return new ApiResponse<>(count, updateAt, data);
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(1, LocalDateTime.now(), data);
    }
}
