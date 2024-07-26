package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.ResponsePathDTO;
import cleanbreath.backend.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseAllAddressDTO {
    private Long id;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<ResponsePathDTO> path;

    public ResponseAllAddressDTO(Address address) {
        this.id = address.getId();
        this.addressName = address.getAddressName();
        this.buildingName = address.getBuildingName();
        this.latitude = address.getAddressPosLat();
        this.longitude = address.getAddressPosLng();
        this.category = address.getAddressCategory();
        this.path = address.getPaths().stream().map(ResponsePathDTO::new).toList();
    }
}
