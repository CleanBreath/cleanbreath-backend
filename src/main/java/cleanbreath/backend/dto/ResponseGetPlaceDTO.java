package cleanbreath.backend.dto;

import cleanbreath.backend.entity.DivisionArea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseGetPlaceDTO {
    private Long id;
    private String addressName;
    private String buildingName;
    private Double latitude;
    private Double longitude;
    private DivisionArea divisionArea;
    private String pathLat;
    private String pathLng;
}
