package cleanbreath.backend.dto.ApartmentDTO;

import cleanbreath.backend.entity.ApartmentPath;
import cleanbreath.backend.entity.NonSmokingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseAllApartmentPathDTO {
    private NonSmokingStatus hallway;
    private NonSmokingStatus stairs;
    private NonSmokingStatus elevator;
    private NonSmokingStatus underground_parking_lot;
    private Double latitude;
    private Double longitude;
    private String pathsLat;
    private String pathsLng;

    public ResponseAllApartmentPathDTO(ApartmentPath apartmentPath) {
        this.hallway = apartmentPath.getHallway();
        this.stairs = apartmentPath.getStairs();
        this.elevator = apartmentPath.getElevator();
        this.underground_parking_lot = apartmentPath.getUndergroundParkingLot();
        this.latitude = apartmentPath.getApartmentLat();
        this.longitude = apartmentPath.getApartmentLng();
        this.pathsLat = apartmentPath.getPathsLat();
        this.pathsLng = apartmentPath.getPathsLng();
    }
}
