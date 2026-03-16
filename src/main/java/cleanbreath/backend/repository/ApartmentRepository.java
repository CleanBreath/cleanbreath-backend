package cleanbreath.backend.repository;

import cleanbreath.backend.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query("select distinct a from Apartment a left join fetch a.apartmentPaths where a.region = :region")
    List<Apartment> findByRegion(String region);

    // N+1 문제 방지 - 전체 아파트 조회 시 apartmentPaths를 함께 로딩
    @Query("select distinct a from Apartment a left join fetch a.apartmentPaths")
    List<Apartment> findAllWithPaths();
}
