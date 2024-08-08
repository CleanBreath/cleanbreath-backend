package cleanbreath.backend.dto.PathDTO;

import cleanbreath.backend.entity.DivisionArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPathDTO {
    private DivisionArea divisionArea;
    private String pathLat;
    private String pathLng;
}
