package cleanbreath.backend.repository;

import cleanbreath.backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddressPosLatAndAddressPosLng(Double lat, Double lng);
    
    @Query("SELECT DISTINCT a FROM Address a LEFT JOIN FETCH a.paths")
    List<Address> findAllWithPaths();

    @Query("SELECT DISTINCT a FROM Address a LEFT JOIN FETCH a.paths WHERE a.id IN :ids")
    List<Address> findAllWithPathsByIdIn(Collection<Long> ids);

    // 대표 좌표가 사각형 안에 있는 구역. 공간 인덱스 idx_address_location 사용
    @Query(value = "SELECT address_id FROM address " +
            "WHERE MBRContains(ST_MakeEnvelope(POINT(:swLng, :swLat), POINT(:neLng, :neLat)), location)",
            nativeQuery = true)
    List<Long> findIdsWithin(double swLat, double swLng, double neLat, double neLng);

    // 다각형이 이 점을 포함하는 구역. 공간 인덱스 idx_path_area 사용
    @Query(value = "SELECT DISTINCT address_id FROM path WHERE ST_Contains(area, POINT(:lng, :lat))",
            nativeQuery = true)
    List<Long> findIdsContaining(double lat, double lng);
}
