package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "address_info")
@Getter
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

    @OneToOne(mappedBy = "address_info", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BuildingInfo buildingInfo;

    @OneToOne(mappedBy = "address_info", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AreaInfo areaInfo;

    @OneToOne(mappedBy = "address_info", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AddressPathsInfo addressPathsInfo;
}
