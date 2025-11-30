package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.PendingPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingPathRepository extends JpaRepository<PendingPath, Long> {
//    void deleteByManageAddressId(Long id);
//    Optional<PendingPath> findByPendingAddress(Long addressId);
}
