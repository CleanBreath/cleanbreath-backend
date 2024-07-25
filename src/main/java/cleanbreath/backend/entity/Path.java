package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "path")
@Getter
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

    @Column(name = "path_lat")
    private String pathLat;

    @Column(name = "path_lng")
    private String pathLng;
}
