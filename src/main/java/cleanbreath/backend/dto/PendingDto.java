package cleanbreath.backend.dto;

import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class PendingDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AddressResponse {
        private Long id;
        private String addressName;
        private String buildingName;
        private Double latitude;
        private Double longitude;
        private String category;
        private List<PathResponse> paths;

        public AddressResponse(PendingAddress pendingAddress) {
            this.id = pendingAddress.getId();
            this.addressName = pendingAddress.getAddressName();
            this.buildingName = pendingAddress.getBuildingName();
            this.latitude = pendingAddress.getAddressPosLat();
            this.longitude = pendingAddress.getAddressPosLng();
            this.category = pendingAddress.getAddressCategory();
            this.paths = pendingAddress.getPaths()
                    .stream()
                    .map(PathResponse::new)
                    .toList();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PathResponse {
        private DivisionArea divisionArea;
        private String pathLat;
        private String pathLng;

        public PathResponse(PendingPath pendingPath) {
            this.divisionArea = pendingPath.getDivisionArea();
            this.pathLat = pendingPath.getPathLat();
            this.pathLng = pendingPath.getPathLng();
        }
    }
}
