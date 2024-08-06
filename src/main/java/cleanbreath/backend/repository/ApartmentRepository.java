package cleanbreath.backend.repository;

import cleanbreath.backend.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByRegion(String region);
}
