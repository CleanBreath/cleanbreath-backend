package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "address_path_info")
@Getter
public class AddressPathsInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Long id;

    @Column(name = "path_lat")
    private String pathLat;

    @Column(name = "path_lng")
    private String pathLng;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_info")
    private AddressInfo addressInfo;
}
