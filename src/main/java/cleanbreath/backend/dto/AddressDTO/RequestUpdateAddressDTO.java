package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.RequestPathDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestUpdateAddressDTO {
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<RequestPathDTO> paths;
}
