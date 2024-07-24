package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "building_info")
@Getter
public class BuildingInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    @Column(name = "main_building_no")
    private Integer mainBuildingNo;

    @Column(name = "sub_building_no")
    private Integer subBuildingNo;

    @Column(name = "building_name", length = 45)
    private String buildingName;

    @Column(name = "zone_no")
    private Integer zoneNo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @JsonIgnore
    private AddressInfo addressInfo;
}
