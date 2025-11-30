package cleanbreath.backend.dto;

import cleanbreath.backend.entity.Apartment;
import cleanbreath.backend.entity.ApartmentPath;
import cleanbreath.backend.entity.NonSmokingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class ApartmentDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String region;
        private String designationNumber;
        private String apartmentName;
        private String address;
        private int numberOfBuilding;
        private int numberOfHouseholds;
        private LocalDate designationDate;
        private List<PathResponse> path;

        public Response(Apartment apartment) {
            this.id = apartment.getId();
            this.region = apartment.getRegion();
            this.designationNumber = apartment.getDesignationNumber();
            this.apartmentName = apartment.getApartmentName();
            this.address = apartment.getAddress();
            this.numberOfBuilding = apartment.getNumberOfBuilding();
            this.numberOfHouseholds = apartment.getNumberOfHouseholds();
            this.designationDate = apartment.getDesignationDate();
            this.path = apartment.getApartmentPaths()
                    .stream()
                    .map(PathResponse::new)
                    .toList();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PathResponse {
        private NonSmokingStatus hallway;
        private NonSmokingStatus stairs;
        private NonSmokingStatus elevator;
        private NonSmokingStatus undergroundParkingLot;
        private Double latitude;
        private Double longitude;
        private String pathsLat;
        private String pathsLng;

        public PathResponse(ApartmentPath apartmentPath) {
            this.hallway = apartmentPath.getHallway();
            this.stairs = apartmentPath.getStairs();
            this.elevator = apartmentPath.getElevator();
            this.undergroundParkingLot = apartmentPath.getUndergroundParkingLot();
            this.latitude = apartmentPath.getApartmentLat();
            this.longitude = apartmentPath.getApartmentLng();
            this.pathsLat = apartmentPath.getPathsLat();
            this.pathsLng = apartmentPath.getPathsLng();
        }
    }
}
