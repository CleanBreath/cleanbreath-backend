package cleanbreath.backend.repository.manage;

import cleanbreath.backend.entity.manage.ManageAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageAddressRepository extends JpaRepository<ManageAddress, Long> {
}
