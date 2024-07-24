package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "address_info")
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

    @OneToMany(mappedBy = "addressInfo", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BuildingInfo> buildingInfo = new ArrayList<>();

    @OneToMany(mappedBy = "addressInfo", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AreaInfo> areaInfo = new ArrayList<>();

    @OneToMany(mappedBy = "addressInfo", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AddressPathsInfo> addressPathsInfo = new ArrayList<>();
}
