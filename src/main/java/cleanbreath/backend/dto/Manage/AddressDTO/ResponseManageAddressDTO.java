package cleanbreath.backend.dto.Manage.AddressDTO;

import cleanbreath.backend.dto.Manage.PathDTO.ResponseManagePathDTO;
import cleanbreath.backend.entity.pending.PendingAddress;
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

    public ResponseManageAddressDTO(PendingAddress pendingAddress) {
        this.id = pendingAddress.getId();
        this.addressName = pendingAddress.getAddressName();
        this.buildingName = pendingAddress.getBuildingName();
        this.latitude = pendingAddress.getAddressPosLat();
        this.longitude = pendingAddress.getAddressPosLng();
        this.category = pendingAddress.getAddressCategory();
        this.paths = pendingAddress.getPaths()
                .stream()
                .map(ResponseManagePathDTO::new)
                .toList();
    }
}
