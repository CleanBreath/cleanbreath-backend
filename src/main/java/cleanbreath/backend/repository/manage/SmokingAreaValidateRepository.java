package cleanbreath.backend.repository.manage;

import cleanbreath.backend.entity.manage.SmokingAreaValidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmokingAreaValidateRepository extends JpaRepository<SmokingAreaValidate, Long> {
}
