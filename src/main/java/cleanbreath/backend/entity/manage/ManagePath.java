package cleanbreath.backend.entity.manage;

import cleanbreath.backend.entity.DivisionArea;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ManagePath {
    @Column(name = "m_path")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "m_address_id") @JsonIgnore
    private ManageAddress manageAddress;

    @Column(name = "m_division_area")
    @Enumerated(EnumType.STRING)
    private DivisionArea divisionArea;

    @Column(name = "m_path_lat", columnDefinition = "MEDIUMTEXT")
    private String pathLat;

    @Column(name = "m_path_lng", columnDefinition = "MEDIUMTEXT")
    private String pathLng;

    public void updatePath(DivisionArea divisionArea, String pathLat, String pathLng) {
        this.divisionArea = divisionArea;
        this.pathLat = pathLat;
        this.pathLng = pathLng;
    }
}
