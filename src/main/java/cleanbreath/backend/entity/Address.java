package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "address_pos_lat")
    private Double addressPosLat;

    @Column(name = "address_pos_lng")
    private Double addressPosLng;

    @Column(name = "address_category")
    private String addressCategory;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Path> paths = new ArrayList<>();

}
