package cleanbreath.backend.dto.Pending.PathDTO;

import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.pending.PendingPath;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseManagePathDTO {
    private DivisionArea divisionArea;
    private String pathLat;
    private String pathLng;

    public ResponseManagePathDTO(PendingPath pendingPath) {
        this.divisionArea = pendingPath.getDivisionArea();
        this.pathLat = pendingPath.getPathLat();
        this.pathLng = pendingPath.getPathLng();
    }
}
