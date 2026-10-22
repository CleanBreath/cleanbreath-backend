package cleanbreath.backend.repository;

import cleanbreath.backend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // 보관 기간이 지난 피드백 파기용
    long deleteByCreateAtBefore(LocalDateTime cutoff);
}
