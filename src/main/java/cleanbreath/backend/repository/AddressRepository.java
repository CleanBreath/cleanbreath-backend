package cleanbreath.backend.repository;

import cleanbreath.backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddressPosLatAndAddressPosLng(Double lat, Double lng);
}
