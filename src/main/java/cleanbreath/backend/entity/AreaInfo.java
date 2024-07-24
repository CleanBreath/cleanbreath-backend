package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "area_info")
@Getter
public class AreaInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Long id;

    @Column(name = "division_area")
    private String divisionArea;

    @Column(name = "implicit_area")
    private String implicitArea;

    @JsonIgnore
    @OneToOne(mappedBy = "areaInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AddressInfo addressInfo;

//    public AreaInfo inputAreaInfo(String divisionArea, String implicitArea) {
//        this.divisionArea = divisionArea;
//        this.implicitArea = implicitArea;
//    }

}
