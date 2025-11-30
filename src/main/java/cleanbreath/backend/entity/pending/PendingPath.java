package cleanbreath.backend.entity.pending;

import cleanbreath.backend.entity.DivisionArea;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PendingPath {
    @Column(name = "u_path_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "u_address_id") @JsonIgnore
    private PendingAddress pendingAddress;

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
