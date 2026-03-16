package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.PendingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingAddressRepository extends JpaRepository<PendingAddress, Long> {
    @Query("SELECT DISTINCT p FROM PendingAddress p LEFT JOIN FETCH p.paths")
    List<PendingAddress> findAllWithPaths();
}
