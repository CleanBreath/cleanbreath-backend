package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "path", indexes = {
    // 외래키 인덱스 - findByAddressId 및 JOIN FETCH 최적화
    @Index(name = "idx_path_address_id", columnList = "address_id")
})
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Path {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id") @JsonIgnore
    private Address address;

    @Column(name = "division_area")
    @Enumerated(EnumType.STRING)
    private DivisionArea divisionArea;

    @Column(name = "path_lat", columnDefinition = "MEDIUMTEXT")
    private String pathLat;

    @Column(name = "path_lng", columnDefinition = "MEDIUMTEXT")
    private String pathLng;
}
