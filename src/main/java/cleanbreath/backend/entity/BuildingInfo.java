package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "building_info")
@Getter
public class BuildingInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_info")
    private Long id;

    @Column(name = "main_building_no")
    private Integer mainBuildingNo;

    @Column(name = "sub_building_no")
    private Integer subBuildingNo;

    @Column(name = "building_name", length = 45)
    private String buildingName;

    @Column(name = "zone_no")
    private Integer zoneNo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_info")
    private AddressInfo addressInfo;
}
