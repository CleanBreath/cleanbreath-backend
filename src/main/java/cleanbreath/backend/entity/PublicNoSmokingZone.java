package cleanbreath.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 공공데이터포털 전국금연구역표준데이터의 실외 금연구역(점 좌표). 읽기 전용이다.
 * 테이블과 공간 인덱스: infra/sql/2026-10-26-public-no-smoking-zone.sql, 적재: infra/scripts/public-no-smoking-zone.mjs
 */
@Entity
@Table(name = "public_no_smoking_zone")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicNoSmokingZone {

    @Id
    @Column(name = "zone_id")
    private Long id;

    private String name;

    @Column(name = "zone_type")
    private String zoneType;

    private String division;

    private String scope;

    private String sido;

    private String sigungu;

    @Column(name = "road_address")
    private String roadAddress;

    private Double lat;

    private Double lng;

    @Column(name = "reference_date")
    private LocalDate referenceDate;
}
