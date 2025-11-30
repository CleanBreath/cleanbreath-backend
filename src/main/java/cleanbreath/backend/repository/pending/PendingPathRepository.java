package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingPathRepository extends JpaRepository<PendingPath, Long> {
    Optional<PendingPath> findByPendingAddress(PendingAddress address);
    void deleteByPendingAddress(Long id);
}
