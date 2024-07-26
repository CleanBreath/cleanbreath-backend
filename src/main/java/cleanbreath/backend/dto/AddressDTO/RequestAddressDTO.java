package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.ResponsePathDTO;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RequestAddressDTO {
    private Long id;
    private LocalDateTime updateAt;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<Path> paths;

    public Address toEntity() {
        return Address.builder()
                .id(this.id)
                .updateAt(this.updateAt)
                .addressName(this.addressName)
                .buildingName(this.buildingName)
                .addressPosLat(this.latitude)
                .addressPosLng(this.longitude)
                .addressCategory(this.category)
                .paths(this.paths)
                .build();
    }
}
