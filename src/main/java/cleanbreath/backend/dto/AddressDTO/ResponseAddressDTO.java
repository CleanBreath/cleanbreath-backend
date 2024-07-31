package cleanbreath.backend.dto.AddressDTO;

import cleanbreath.backend.dto.PathDTO.ResponsePathDTO;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class ResponseAddressDTO {
    private Long id;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<ResponsePathDTO> path;

    public ResponseAddressDTO(Address address) {
        this.id = address.getId();
        this.addressName = address.getAddressName();
        this.buildingName = address.getBuildingName();
        this.latitude = address.getAddressPosLat();
        this.longitude = address.getAddressPosLng();
        this.category = address.getAddressCategory();
        this.path = address.getPaths().stream().map(ResponsePathDTO::new).toList();;
    }
}
