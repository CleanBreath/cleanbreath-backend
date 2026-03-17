package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.PendingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingAddressRepository extends JpaRepository<PendingAddress, Long> {
    @Query("SELECT DISTINCT p FROM PendingAddress p LEFT JOIN FETCH p.paths")
    List<PendingAddress> findAllWithPaths();

    // 검증 투표(truth) 합계가 임계값 이상인 PendingAddress 조회 (배치용)
    @Query("""
        SELECT DISTINCT pa FROM PendingAddress pa
        LEFT JOIN FETCH pa.paths
        JOIN pa.areaValidationRequests avr
        GROUP BY pa
        HAVING SUM(avr.truth) >= :threshold
    """)
    List<PendingAddress> findAddressesMeetingThreshold(@Param("threshold") int threshold);
}
