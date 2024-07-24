package cleanbreath.backend.repository;

import cleanbreath.backend.entity.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {
}
