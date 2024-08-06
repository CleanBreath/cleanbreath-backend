package cleanbreath.backend.entity.manage;

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
public class ManageAddress {
    @Column(name = "m_address_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청한 사용자 이름
    @Column(name = "r_username")
    private String username;

    // 요청한 사용자 비밀번호
    @Column(name = "r_password")
    private String password;

    @Column(name = "m_update_at")
    private LocalDateTime updateAt;

    @Column(name = "m_address_name")
    private String addressName;

    @Column(name = "m_building_name")
    private String buildingName;

    @Column(name = "m_address_pos_lat")
    private Double addressPosLat;

    @Column(name = "m_address_pos_lng")
    private Double addressPosLng;

    @Column(name = "m_address_category")
    private String addressCategory;

    @Builder.Default
    @OneToMany(mappedBy = "manageAddress", fetch = FetchType.LAZY, cascade = ALL)
    private List<SmokingAreaValidate> smokingAreaValidates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "manageAddress", fetch = FetchType.LAZY, cascade = ALL)
    private List<ManagePath> paths = new ArrayList<>();

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
