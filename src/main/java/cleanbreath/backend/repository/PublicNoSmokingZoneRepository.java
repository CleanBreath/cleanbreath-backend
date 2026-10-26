package cleanbreath.backend.repository;

import cleanbreath.backend.entity.PublicNoSmokingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicNoSmokingZoneRepository extends JpaRepository<PublicNoSmokingZone, Long> {

    // 사각형 안의 점. 공간 인덱스 idx_public_zone_location 사용
    @Query(value = "SELECT * FROM public_no_smoking_zone " +
            "WHERE MBRContains(ST_MakeEnvelope(POINT(:swLng, :swLat), POINT(:neLng, :neLat)), location) " +
            "LIMIT :limit",
            nativeQuery = true)
    List<PublicNoSmokingZone> findWithin(double swLat, double swLng, double neLat, double neLng, int limit);
}
