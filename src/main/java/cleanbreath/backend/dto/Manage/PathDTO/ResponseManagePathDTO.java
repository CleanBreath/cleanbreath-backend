package cleanbreath.backend.dto.Manage.PathDTO;

import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.manage.ManagePath;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseManagePathDTO {
    private DivisionArea divisionArea;
    private String pathLat;
    private String pathLng;

    public ResponseManagePathDTO(ManagePath managePath) {
        this.divisionArea = managePath.getDivisionArea();
        this.pathLat = managePath.getPathLat();
        this.pathLng = managePath.getPathLng();
    }
}
