package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "path_info")
@Getter
public class AddressPathsInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Long id;

    @Column(name = "path_lat")
    private String pathLat;

    @Column(name = "path_lng")
    private String pathLng;

    @JsonIgnore
    @OneToOne(mappedBy = "addressPathsInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AddressInfo addressInfo;

}
