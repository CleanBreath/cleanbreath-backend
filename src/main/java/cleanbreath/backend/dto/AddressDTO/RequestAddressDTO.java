package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.ResponsePathDTO;
import cleanbreath.backend.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestAddressDTO {
    private LocalDateTime updateAt;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
}
