package cleanbreath.backend.dto.ApartmentDTO;

import cleanbreath.backend.entity.Apartment;
import cleanbreath.backend.entity.NonSmokingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class ResponseAllApartmentDTO {
    private Long id;
    private String region;
    private String designationNumber;
    private String apartmentName;
    private String address;
    private int numberOfBuilding;
    private int numberOfHouseholds;
    private LocalDate designationDate;
    private List<ResponseAllApartmentPathDTO> path;

    public ResponseAllApartmentDTO(Apartment apartment) {
        this.id = apartment.getId();
        this.region = apartment.getRegion();
        this.designationNumber = apartment.getDesignationNumber();
        this.apartmentName = apartment.getApartmentName();
        this.address = apartment.getAddress();
        this.numberOfBuilding = apartment.getNumberOfBuilding();
        this.numberOfHouseholds = apartment.getNumberOfHouseholds();
        this.designationDate = apartment.getDesignationDate();
        this.path = apartment
                .getApartmentPaths()
                .stream()
                .map(ResponseAllApartmentPathDTO::new)
                .toList();
    }
}
