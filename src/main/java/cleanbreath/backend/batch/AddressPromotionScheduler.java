package cleanbreath.backend.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 매일 새벽 3시에 주소 승격 배치를 실행하는 스케줄러.
 * Spring Batch 5.x 권장 방식인 JobOperator를 사용합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AddressPromotionScheduler {

  private final JobOperator jobOperator;
  private final Job addressPromotionJob;

  // 매일 새벽 3시 실행
  @Scheduled(cron = "${batch.promotion.cron:0 0 3 * * *}")
  public void runPromotionBatch() {
    try {
      // 매 실행마다 고유한 JobParameters 생성
      JobParameters params = new JobParametersBuilder()
          .addLong("executionTime", System.currentTimeMillis())
          .toJobParameters();

      JobExecution executionId = jobOperator.start(addressPromotionJob, params);
      log.info("[스케줄러] 주소 승격 배치 실행 완료 (executionId: {})",
          executionId);
    } catch (Exception e) {
      log.error("[스케줄러] 주소 승격 배치 실행 실패: {}",
          e.getMessage(), e);
    }
  }
}
