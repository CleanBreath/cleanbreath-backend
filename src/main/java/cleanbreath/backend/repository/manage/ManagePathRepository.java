package cleanbreath.backend.repository.manage;

import cleanbreath.backend.entity.manage.ManagePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagePathRepository extends JpaRepository<ManagePath, Long> {
    void deleteByManageAddressId(Long id);
    Optional<ManagePath> findByManageAddressId(Long addressId);
}
