package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.RequestPathDTO;
import cleanbreath.backend.dto.PathDTO.ResponsePathDTO;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class RequestAddressDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateAt;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<RequestPathDTO> paths;

    public Address toEntity() {
        return Address.builder()
                .addressName(addressName)
                .buildingName(buildingName)
                .updateAt(updateAt)
                .addressPosLat(latitude)
                .addressPosLng(longitude)
                .addressCategory(category)
                .build();
    }
}
