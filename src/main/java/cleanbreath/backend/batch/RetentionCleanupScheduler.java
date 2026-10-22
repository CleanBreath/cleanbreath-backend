package cleanbreath.backend.batch;

import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.repository.FeedbackRepository;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.repository.pending.SmokingAreaValidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 개인정보처리방침의 보관 기간이 지난 데이터를 매일 파기하는 스케줄러.
 * 피드백은 접수 후 1년, 승인되지 않은 구역 요청은 투표 기록과 함께 등록 후 6개월에 파기합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RetentionCleanupScheduler {

  private final FeedbackRepository feedbackRepository;
  private final PendingAddressRepository pendingAddressRepository;
  private final PendingPathRepository pendingPathRepository;
  private final SmokingAreaValidateRepository validationRepository;

  // 승격 배치(새벽 3시) 뒤에 실행해 승격 대상이 먼저 빠지도록 함
  @Scheduled(cron = "${batch.retention.cron:0 30 3 * * *}", zone = "Asia/Seoul")
  @Transactional
  public void purgeExpired() {
    LocalDateTime now = LocalDateTime.now();

    long feedbacks = feedbackRepository.deleteByCreateAtBefore(now.minusYears(1));

    List<PendingAddress> expired = pendingAddressRepository.findByUpdateAtBefore(now.minusMonths(6));
    for (PendingAddress pending : expired) {
      validationRepository.deleteByPendingAddress(pending);
      pendingPathRepository.deleteByPendingAddress(pending);
      pendingAddressRepository.delete(pending);
    }

    log.info("[보관기간] 피드백 {}건, 미승인 요청 {}건 파기", feedbacks, expired.size());
  }
}
