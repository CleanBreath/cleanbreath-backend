package cleanbreath.backend.repository;

import cleanbreath.backend.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
    Path findByAddressId(Long addressId);
}
