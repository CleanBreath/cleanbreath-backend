package cleanbreath.backend.dto;

import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.entity.pending.PendingAddress;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class AddressDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updateAt;
        private String addressName;
        private String buildingName;
        private Double latitude;
        private Double longitude;
        private String category;
        private List<PathRequest> paths;

        public PendingAddress toEntity() {
            return PendingAddress.builder()
                    .addressName(addressName)
                    .buildingName(buildingName)
                    .updateAt(updateAt)
                    .addressPosLat(latitude)
                    .addressPosLng(longitude)
                    .addressCategory(category)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Update {
        private String addressName;
        private String buildingName;
        private Double latitude;
        private Double longitude;
        private String category;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CheckUpdate {
        private LocalDateTime updateDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String addressName;
        private String buildingName;
        private Double latitude;
        private Double longitude;
        private String category;
        private List<PathResponse> path;

        public Response(Address address) {
            this.id = address.getId();
            this.addressName = address.getAddressName();
            this.buildingName = address.getBuildingName();
            this.latitude = address.getAddressPosLat();
            this.longitude = address.getAddressPosLng();
            this.category = address.getAddressCategory();
            this.path = address.getPaths().stream().map(PathResponse::new).toList();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ListResponse {
        private Long id;
        private String addressName;
        private String buildingName;
        private Double latitude;
        private Double longitude;
        private String category;
        private List<PathResponse> path;

        public ListResponse(Address address) {
            this.id = address.getId();
            this.addressName = address.getAddressName();
            this.buildingName = address.getBuildingName();
            this.latitude = address.getAddressPosLat();
            this.longitude = address.getAddressPosLng();
            this.category = address.getAddressCategory();
            this.path = address.getPaths().stream().map(PathResponse::new).toList();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PathRequest {
        private DivisionArea divisionArea;
        private String pathLat;
        private String pathLng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PathResponse {
        private DivisionArea divisionArea;
        private String pathsLatitude;
        private String pathsLongitude;

        public PathResponse(Path path) {
            this.divisionArea = path.getDivisionArea();
            this.pathsLatitude = path.getPathLat();
            this.pathsLongitude = path.getPathLng();
        }
    }
}
