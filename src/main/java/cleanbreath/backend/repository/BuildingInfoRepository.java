package cleanbreath.backend.repository;

import cleanbreath.backend.entity.BuildingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingInfoRepository extends JpaRepository<BuildingInfo, Long> {
}
