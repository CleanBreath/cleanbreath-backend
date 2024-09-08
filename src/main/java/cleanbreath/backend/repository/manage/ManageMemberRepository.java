package cleanbreath.backend.repository.manage;

import cleanbreath.backend.entity.manage.ManageMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManageMemberRepository extends JpaRepository<ManageMember, Long> {
    Optional<ManageMember> findByUsername(String username);
}
