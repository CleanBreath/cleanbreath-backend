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
    private Double apartmentLat;
    private Double apartmentLng;

    public ResponseAllApartmentPathDTO(ApartmentPath apartmentPath) {
        this.hallway = apartmentPath.getHallway();
        this.stairs = apartmentPath.getStairs();
        this.elevator = apartmentPath.getElevator();
        this.apartmentLat = apartmentPath.getApartmentLat();
        this.apartmentLng = apartmentPath.getApartmentLng();
    }
}
