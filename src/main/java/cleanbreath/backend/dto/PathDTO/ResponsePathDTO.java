package cleanbreath.backend.dto.PathDTO;

import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.Path;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePathDTO {
    private DivisionArea divisionArea;
    private String pathsLatitude;
    private String pathsLongitude;

    public ResponsePathDTO(Path path) {
        this.divisionArea = path.getDivisionArea();
        this.pathsLatitude = path.getPathLat();
        this.pathsLongitude = path.getPathLng();
    }
}
