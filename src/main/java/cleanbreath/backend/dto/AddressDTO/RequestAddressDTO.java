package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.RequestPathDTO;
import cleanbreath.backend.entity.manage.ManageAddress;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RequestAddressDTO {
    private String username;
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateAt;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<RequestPathDTO> paths;

    public ManageAddress toEntity() {
        return ManageAddress.builder()
                .username(username)
                .password(password)
                .addressName(addressName)
                .buildingName(buildingName)
                .updateAt(updateAt)
                .addressPosLat(latitude)
                .addressPosLng(longitude)
                .addressCategory(category)
                .build();
    }
}
