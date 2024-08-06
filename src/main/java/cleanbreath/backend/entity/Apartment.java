package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "apartment")
@AllArgsConstructor
public class Apartment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Long id;

    @Column(name = "region")
    private String region; // 지역 이름

    @Column(name = "designation_number")
    private String designationNumber; // 지정 번호

    @Column(name = "apartment_name")
    private String apartmentName; // 아파트 이름

    @Column(name = "address")
    private String address; // 아파트 주소

    @Column(name = "number_of_building")
    private int numberOfBuilding; // 동 수

    @Column(name = "number_of_households")
    private int numberOfHouseholds; // 세대 수

    @Column(name = "designation_date")
    private LocalDate designationDate; // 지정 일자

    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ApartmentPath> apartmentPaths = new ArrayList<>();
}
