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

    /**
     * 구역 상세 조회 응답 (메타데이터 + 영역 데이터 + 진위 투표 수)
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailResponse {
        private Long id;
        private String addressName;
        private String buildingName;
        private Double latitude;
        private Double longitude;
        private String category;
        private List<PathResponse> paths;
        private int truthCount;
        private int untruthCount;

        public DetailResponse(PendingAddress pendingAddress,
                              int truthCount, int untruthCount) {
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
            this.truthCount = truthCount;
            this.untruthCount = untruthCount;
        }
    }

    /**
     * 진위 투표 요청
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ValidationVoteRequest {
        private String clientToken;
        private boolean isTruth;
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
