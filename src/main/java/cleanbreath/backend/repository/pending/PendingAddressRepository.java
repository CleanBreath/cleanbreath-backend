package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.PendingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PendingAddressRepository extends JpaRepository<PendingAddress, Long> {
    @Query("SELECT DISTINCT p FROM PendingAddress p LEFT JOIN FETCH p.paths")
    List<PendingAddress> findAllWithPaths();

    // 보관 기간이 지난 미승인 요청 조회 (파기용)
    List<PendingAddress> findByUpdateAtBefore(LocalDateTime cutoff);

    // 검증 투표(truth) 합계가 임계값 이상인 PendingAddress 조회 (배치용)
    // 합계는 서브쿼리에서 구한다: paths를 함께 GROUP BY하면 only_full_group_by에서 실패하고, 경로 수만큼 합계가 부풀려진다
    @Query("""
        SELECT DISTINCT pa FROM PendingAddress pa
        LEFT JOIN FETCH pa.paths
        WHERE pa.id IN (
            SELECT avr.pendingAddress.id FROM AreaValidationRequest avr
            GROUP BY avr.pendingAddress.id
            HAVING SUM(avr.truth) >= :threshold
        )
    """)
    List<PendingAddress> findAddressesMeetingThreshold(@Param("threshold") int threshold);
}
