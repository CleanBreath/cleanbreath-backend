package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "apartment_path")
public class ApartmentPath {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_path_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "apartment_id") @JsonIgnore
    private Apartment apartment;

    /**
     * 금연여부
     */
    @Column(name = "hallway")
    @Enumerated(EnumType.STRING)
    private NonSmokingStatus hallway; // 복도

    @Column(name = "stairs")
    @Enumerated(EnumType.STRING)
    private NonSmokingStatus stairs; // 계단

    @Enumerated(EnumType.STRING)
    @Column(name = "elevator")
    private NonSmokingStatus elevator; // 엘리베이터

    @Enumerated(EnumType.STRING)
    @Column(name = "underground_parking_lot")
    private NonSmokingStatus undergroundParkingLot; // 지하주차장

    @Column(name = "apartment_lat")
    private Double apartmentLat;

    @Column(name = "apartment_lng")
    private Double apartmentLng;
}
