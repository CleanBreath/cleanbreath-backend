package cleanbreath.backend.repository.pending;

import cleanbreath.backend.entity.pending.AreaValidationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmokingAreaValidateRepository extends JpaRepository<AreaValidationRequest, Long> {
}
