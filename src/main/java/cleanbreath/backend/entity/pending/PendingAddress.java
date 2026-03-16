package cleanbreath.backend.entity.pending;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "pending_address")
public class PendingAddress {
    @Column(name = "u_address_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "u_update_at")
    private LocalDateTime updateAt;

    @Column(name = "u_address_name")
    private String addressName;

    @Column(name = "u_building_name")
    private String buildingName;

    @Column(name = "u_address_pos_lat")
    private Double addressPosLat;

    @Column(name = "u_address_pos_lng")
    private Double addressPosLng;

    @Column(name = "u_address_category")
    private String addressCategory;

    @Builder.Default
    @OneToMany(mappedBy = "pendingAddress", fetch = FetchType.LAZY, cascade = ALL)
    private List<AreaValidationRequest> areaValidationRequests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pendingAddress", fetch = FetchType.LAZY, cascade = ALL)
    private List<PendingPath> paths = new ArrayList<>();

    public void updateManageAddress(String addressName,
                                    String buildingName,
                                    Double addressPosLat,
                                    Double addressPosLng,
                                    String addressCategory) {
        this.addressName = addressName;
        this.buildingName = buildingName;
        this.addressPosLat = addressPosLat;
        this.addressPosLng = addressPosLng;
        this.addressCategory = addressCategory;
    }
}
