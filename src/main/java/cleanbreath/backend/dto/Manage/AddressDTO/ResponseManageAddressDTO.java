package cleanbreath.backend.dto.Manage.AddressDTO;

import cleanbreath.backend.dto.Manage.PathDTO.ResponseManagePathDTO;
import cleanbreath.backend.entity.manage.ManageAddress;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ResponseManageAddressDTO {
    private Long id;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private String category;
    private List<ResponseManagePathDTO> paths;

    public ResponseManageAddressDTO(ManageAddress manageAddress) {
        this.id = manageAddress.getId();
        this.addressName = manageAddress.getAddressName();
        this.buildingName = manageAddress.getBuildingName();
        this.latitude = manageAddress.getAddressPosLat();
        this.longitude = manageAddress.getAddressPosLng();
        this.category = manageAddress.getAddressCategory();
        this.paths = manageAddress.getPaths()
                .stream()
                .map(ResponseManagePathDTO::new)
                .toList();
    }
}
