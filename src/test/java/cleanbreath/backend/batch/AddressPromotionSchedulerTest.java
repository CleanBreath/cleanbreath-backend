package cleanbreath.backend.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * AddressPromotionScheduler 단위 테스트.
 * Given-When-Then 패턴 + FIRST 원칙 적용.
 */
@ExtendWith(MockitoExtension.class)
class AddressPromotionSchedulerTest {

  @Mock private JobOperator jobOperator;
  @Mock private Job addressPromotionJob;
  @Mock private JobExecution jobExecution;

  @InjectMocks
  private AddressPromotionScheduler scheduler;

  @Test
  @DisplayName("스케줄러 실행 시 JobOperator.start()가 호출된다")
  void shouldCallJobOperatorStart() throws Exception {
    // Given: JobOperator가 정상 동작
    given(jobOperator.start(eq(addressPromotionJob), any(JobParameters.class)))
        .willReturn(jobExecution);

    // When: 스케줄러 실행
    scheduler.runPromotionBatch();

    // Then: JobOperator.start()가 Job과 JobParameters로 호출됨
    verify(jobOperator).start(eq(addressPromotionJob), any(JobParameters.class));
  }

  @Test
  @DisplayName("배치 실행 중 예외 발생 시 예외가 전파되지 않는다")
  void shouldNotPropagateExceptionOnFailure() throws Exception {
    // Given: JobOperator가 예외를 던짐
    given(jobOperator.start(eq(addressPromotionJob), any(JobParameters.class)))
        .willThrow(new RuntimeException("배치 실행 실패"));

    // When & Then: 예외가 전파되지 않음 (catch 처리)
    scheduler.runPromotionBatch();

    // 메서드가 정상 종료되면 테스트 통과
    verify(jobOperator).start(eq(addressPromotionJob), any(JobParameters.class));
  }
}
