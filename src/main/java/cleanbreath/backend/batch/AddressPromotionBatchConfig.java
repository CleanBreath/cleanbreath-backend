package cleanbreath.backend.batch;

import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.repository.pending.SmokingAreaValidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 매일 새벽 3시에 실행되는 배치 설정.
 * 검증 투표(truth)가 임계값 이상인 PendingAddress를 Address 테이블로 승격시킵니다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AddressPromotionBatchConfig {

    private final PendingAddressRepository pendingAddressRepository;
    private final PendingPathRepository pendingPathRepository;
    private final SmokingAreaValidateRepository validationRepository;
    private final AddressRepository addressRepository;
    private final PathRepository pathRepository;

    @Value("${batch.promotion.threshold}")
    private int promotionThreshold;

    @Bean
    public Job addressPromotionJob(JobRepository jobRepository, Step addressPromotionStep) {
        return new JobBuilder("addressPromotionJob", jobRepository)
            .start(addressPromotionStep)
            .build();
    }

    @Bean
    public Step addressPromotionStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager) {
        return new StepBuilder("addressPromotionStep", jobRepository)
            .tasklet(addressPromotionTasklet(), transactionManager)
            .build();
    }

    @Bean
    public Tasklet addressPromotionTasklet() {
        return (contribution, chunkContext) -> {
            List<PendingAddress> qualified =
                pendingAddressRepository.findAddressesMeetingThreshold(promotionThreshold);

            if (qualified.isEmpty()) {
                log.info("[배치] 승격 대상 주소 없음 (임계값: {})", promotionThreshold);
                return RepeatStatus.FINISHED;
            }

            log.info("[배치] 승격 대상 주소 {}건 발견 (임계값: {})", qualified.size(), promotionThreshold);

            for (PendingAddress pending : qualified) {
                // PendingAddress → Address 변환
                Address address = Address.builder()
                    .updateAt(LocalDateTime.now())
                    .addressName(pending.getAddressName())
                    .buildingName(pending.getBuildingName())
                    .addressPosLat(pending.getAddressPosLat())
                    .addressPosLng(pending.getAddressPosLng())
                    .addressCategory(pending.getAddressCategory())
                    .build();

                Address savedAddress = addressRepository.save(address);

                // PendingPath → Path 변환 (DivisionArea 기준으로 금연/흡연 구역 구분)
                List<Path> paths = pending.getPaths().stream()
                    .map(pendingPath -> Path.builder()
                        .address(savedAddress)
                        .divisionArea(pendingPath.getDivisionArea())
                        .pathLat(pendingPath.getPathLat())
                        .pathLng(pendingPath.getPathLng())
                        .build())
                    .toList();

                pathRepository.saveAll(paths);

                // 승격 완료 후 Pending 데이터 정리
                validationRepository.deleteByPendingAddress(pending);
                pendingPathRepository.deleteByPendingAddress(pending);
                pendingAddressRepository.delete(pending);

                log.info("[배치] 주소 승격 완료: {} ({})",
                    pending.getAddressName(), pending.getBuildingName());
            }

            log.info("[배치] 총 {}건 승격 완료", qualified.size());
            return RepeatStatus.FINISHED;
        };
    }
}
