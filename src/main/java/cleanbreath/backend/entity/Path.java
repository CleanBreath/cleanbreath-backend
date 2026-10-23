package cleanbreath.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "path", indexes = {
    // 외래키 인덱스 - findByAddressId 및 JOIN FETCH 최적화
    @Index(name = "idx_path_address_id", columnList = "address_id")
    // area 공간 인덱스(idx_path_area)는 infra/sql/2026-10-23-spatial-index.sql에서 만든다
})
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Path {
    private static final double MIN_GAP_M = 0.6;     // 앞 점과 이보다 가까우면 같은 점으로 본다
    private static final double CLOSE_GAP_M = 1.5;   // 끝 점이 시작점과 이보다 가까우면 닫으려던 클릭으로 본다
    private static final double M_PER_DEG_LAT = 111_320;

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

    // 공간 검색용 다각형(WKT). path_lat·path_lng에서 만들며 응답에는 쓰지 않는다
    // columnDefinition은 스키마 검증용: MySQL 드라이버가 POLYGON 컬럼을 GEOMETRY로 보고한다
    @ColumnTransformer(read = "ST_AsText(area)", write = "ST_GeomFromText(?)")
    @Column(name = "area", nullable = false, columnDefinition = "GEOMETRY")
    @JsonIgnore
    private String area;

    @PrePersist @PreUpdate
    private void syncArea() {
        this.area = toPolygonWkt(pathLat, pathLng);
    }

    /**
     * 쉼표로 이은 위도·경도 문자열을 POLYGON WKT로 바꾼다. 좌표 순서는 (경도 위도).
     * 끝점이 시작점과 다르면 시작점을 붙여 닫는다.
     * @throws IllegalArgumentException 숫자가 아니거나, 개수가 다르거나, 꼭짓점이 3개 미만일 때
     */
    public static String toPolygonWkt(String pathLat, String pathLng) {
        if (pathLat == null || pathLng == null) {
            throw new IllegalArgumentException("좌표가 비어 있습니다.");
        }
        String[] lats = pathLat.split(",");
        String[] lngs = pathLng.split(",");
        if (lats.length != lngs.length) {
            throw new IllegalArgumentException("위도와 경도 개수가 다릅니다.");
        }

        List<String> points = new ArrayList<>();
        for (int i = 0; i < lats.length; i++) {
            double lat = Double.parseDouble(lats[i].trim());
            double lng = Double.parseDouble(lngs[i].trim());
            if (!(lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180)) {
                throw new IllegalArgumentException("좌표 범위를 벗어났습니다.");
            }
            // toPlainString: 지수 표기(1.0E-4)를 피한다
            points.add(BigDecimal.valueOf(lng).toPlainString() + " " + BigDecimal.valueOf(lat).toPlainString());
        }
        if (!points.getFirst().equals(points.getLast())) {
            points.add(points.getFirst());
        }
        if (points.size() < 4) {
            throw new IllegalArgumentException("꼭짓점이 3개 미만입니다.");
        }
        return "POLYGON((" + String.join(",", points) + "))";
    }

    public record Coordinates(String pathLat, String pathLng) {}

    /**
     * 손으로 그릴 때 생기는 겹친 점을 지운다: 앞 점과 0.6m 미만인 점, 시작점과 1.5m 미만인 끝 점.
     * 이런 점이 닫는 선과 엇갈려 자기교차 다각형이 된다. 남는 점의 문자열은 그대로 둔다.
     * 입력은 toPolygonWkt로 먼저 검사한다.
     */
    public static Coordinates removeNearDuplicatePoints(String pathLat, String pathLng) {
        String[] lats = pathLat.split(",");
        String[] lngs = pathLng.split(",");
        List<Integer> kept = new ArrayList<>(List.of(0));
        for (int i = 1; i < lats.length; i++) {
            if (distanceM(lats, lngs, i, kept.getLast()) >= MIN_GAP_M) {
                kept.add(i);
            }
        }
        while (kept.size() > 3 && distanceM(lats, lngs, kept.getLast(), 0) < CLOSE_GAP_M) {
            kept.removeLast();
        }
        return new Coordinates(
                String.join(",", kept.stream().map(i -> lats[i].trim()).toList()),
                String.join(",", kept.stream().map(i -> lngs[i].trim()).toList()));
    }

    // 짧은 거리라 평면 근사로 충분하다
    private static double distanceM(String[] lats, String[] lngs, int i, int j) {
        double lat1 = Double.parseDouble(lats[i].trim());
        double lat2 = Double.parseDouble(lats[j].trim());
        double dy = (lat1 - lat2) * M_PER_DEG_LAT;
        double dx = (Double.parseDouble(lngs[i].trim()) - Double.parseDouble(lngs[j].trim()))
                * M_PER_DEG_LAT * Math.cos(Math.toRadians(lat1));
        return Math.hypot(dx, dy);
    }
}
