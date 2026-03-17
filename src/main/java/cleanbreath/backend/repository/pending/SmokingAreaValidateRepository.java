package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.AreaValidationRequest;
import cleanbreath.backend.entity.pending.PendingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmokingAreaValidateRepository extends JpaRepository<AreaValidationRequest, Long> {
    // 동일 클라이언트 토큰 + 대상 ID 중복 체크
    boolean existsByClientTokenAndTargetId(String clientToken, Long targetId);

    // 특정 PendingAddress에 대한 검증 요청 삭제 (배치 승인 후 정리용)
    void deleteByPendingAddress(PendingAddress pendingAddress);

    // 특정 PendingAddress에 대한 truth 합계
    @Query("SELECT COALESCE(SUM(a.truth), 0) FROM AreaValidationRequest a WHERE a.pendingAddress.id = :addressId")
    int sumTruthByPendingAddressId(@Param("addressId") Long addressId);

    // 특정 PendingAddress에 대한 untruth 합계
    @Query("SELECT COALESCE(SUM(a.untruth), 0) FROM AreaValidationRequest a WHERE a.pendingAddress.id = :addressId")
    int sumUntruthByPendingAddressId(@Param("addressId") Long addressId);
}
