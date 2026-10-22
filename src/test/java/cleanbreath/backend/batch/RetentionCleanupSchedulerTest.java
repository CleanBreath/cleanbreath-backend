package cleanbreath.backend.batch;

import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.repository.FeedbackRepository;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.repository.pending.SmokingAreaValidateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * RetentionCleanupScheduler 보관 기간 파기 로직 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class RetentionCleanupSchedulerTest {

  @Mock private FeedbackRepository feedbackRepository;
  @Mock private PendingAddressRepository pendingAddressRepository;
  @Mock private PendingPathRepository pendingPathRepository;
  @Mock private SmokingAreaValidateRepository validationRepository;

  @InjectMocks
  private RetentionCleanupScheduler scheduler;

  @Test
  @DisplayName("피드백은 1년, 미승인 요청은 6개월 기준으로 파기하고 투표·경로도 함께 지운다")
  void purgeExpired_usesPolicyPeriods() {
    PendingAddress expired = PendingAddress.builder().addressName("오래된 요청").build();
    given(pendingAddressRepository.findByUpdateAtBefore(any()))
        .willReturn(List.of(expired));

    scheduler.purgeExpired();

    ArgumentCaptor<LocalDateTime> feedbackCutoff = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<LocalDateTime> pendingCutoff = ArgumentCaptor.forClass(LocalDateTime.class);
    verify(feedbackRepository).deleteByCreateAtBefore(feedbackCutoff.capture());
    verify(pendingAddressRepository).findByUpdateAtBefore(pendingCutoff.capture());

    LocalDateTime now = LocalDateTime.now();
    assertThat(feedbackCutoff.getValue()).isCloseTo(now.minusYears(1), within(1, ChronoUnit.MINUTES));
    assertThat(pendingCutoff.getValue()).isCloseTo(now.minusMonths(6), within(1, ChronoUnit.MINUTES));

    verify(validationRepository).deleteByPendingAddress(expired);
    verify(pendingPathRepository).deleteByPendingAddress(expired);
    verify(pendingAddressRepository).delete(expired);
  }
}
