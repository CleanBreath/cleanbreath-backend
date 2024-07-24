package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "address_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_name", length = 30)
    private String addressName;

    @Column(name = "address_time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "address_pos_lat")
    private Double posLat; // 위도

    @Column(name = "address_pos_lng")
    private Double posLng; // 경도

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "building_id")
    private BuildingInfo buildingInfo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "area_id")
    private AreaInfo areaInfo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "path_id")
    private AddressPathsInfo addressPathsInfo;

    public void InitBuildingInfo(BuildingInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }
    public void InitAreaInfo(AreaInfo areaInfo) {
        this.areaInfo = areaInfo;
    }
    public void InitAddressPathsInfo(AddressPathsInfo addressPathsInfo) {
        this.addressPathsInfo = addressPathsInfo;
    }

    public static AddressInfo createAddress(BuildingInfo buildingInfo, AreaInfo areaInfo, AddressPathsInfo addressPathsInfo) {
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.InitBuildingInfo(buildingInfo);
        addressInfo.InitAreaInfo(areaInfo);
        addressInfo.InitAddressPathsInfo(addressPathsInfo);

        return addressInfo;
    }
}
