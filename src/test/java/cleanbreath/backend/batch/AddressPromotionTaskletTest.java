package cleanbreath.backend.batch;

import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.repository.pending.SmokingAreaValidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * AddressPromotionBatchConfig의 Tasklet 로직 단위 테스트.
 * Given-When-Then 패턴 + FIRST 원칙 적용.
 */
@ExtendWith(MockitoExtension.class)
class AddressPromotionTaskletTest {

  @Mock private PendingAddressRepository pendingAddressRepository;
  @Mock private PendingPathRepository pendingPathRepository;
  @Mock private SmokingAreaValidateRepository validationRepository;
  @Mock private AddressRepository addressRepository;
  @Mock private PathRepository pathRepository;

  @InjectMocks
  private AddressPromotionBatchConfig batchConfig;

  private Tasklet tasklet;

  @BeforeEach
  void setUp() {
    // 임계값 설정
    ReflectionTestUtils.setField(batchConfig, "promotionThreshold", 10);
    tasklet = batchConfig.addressPromotionTasklet();
  }

  @Test
  @DisplayName("승격 대상이 없으면 FINISHED를 반환하고 저장하지 않는다")
  void shouldReturnFinishedWhenNoQualifiedAddresses() throws Exception {
    // Given: 임계값을 충족하는 주소가 없음
    given(pendingAddressRepository.findAddressesMeetingThreshold(10))
        .willReturn(Collections.emptyList());

    // When: Tasklet 실행
    RepeatStatus status = tasklet.execute(null, null);

    // Then: FINISHED 반환, Address 저장 호출 없음
    assertThat(status).isEqualTo(RepeatStatus.FINISHED);
    verify(addressRepository, never()).save(any(Address.class));
    verify(pathRepository, never()).saveAll(anyList());
  }

  @Test
  @DisplayName("임계값 충족 시 PendingAddress가 Address로 승격된다")
  void shouldPromotePendingAddressToAddress() throws Exception {
    // Given: 임계값을 충족하는 PendingAddress 1건 (PendingPath 포함)
    PendingPath pendingPath = PendingPath.builder()
        .divisionArea(DivisionArea.NON_SMOKING_ZONE)
        .pathLat("37.123")
        .pathLng("127.456")
        .build();

    PendingAddress pendingAddress = PendingAddress.builder()
        .addressName("테스트 주소")
        .buildingName("테스트 빌딩")
        .addressPosLat(37.5665)
        .addressPosLng(126.9780)
        .addressCategory("공원")
        .paths(List.of(pendingPath))
        .build();

    given(pendingAddressRepository.findAddressesMeetingThreshold(10))
        .willReturn(List.of(pendingAddress));

    Address savedAddress = Address.builder().id(1L).build();
    given(addressRepository.save(any(Address.class))).willReturn(savedAddress);

    // When: Tasklet 실행
    RepeatStatus status = tasklet.execute(null, null);

    // Then: Address 저장, Path 저장, Pending 데이터 삭제
    assertThat(status).isEqualTo(RepeatStatus.FINISHED);

    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    verify(addressRepository).save(addressCaptor.capture());
    Address captured = addressCaptor.getValue();
    assertThat(captured.getAddressName()).isEqualTo("테스트 주소");
    assertThat(captured.getBuildingName()).isEqualTo("테스트 빌딩");
    assertThat(captured.getAddressPosLat()).isEqualTo(37.5665);
    assertThat(captured.getAddressPosLng()).isEqualTo(126.9780);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Path>> pathsCaptor = ArgumentCaptor.forClass(List.class);
    verify(pathRepository).saveAll(pathsCaptor.capture());
    List<Path> savedPaths = pathsCaptor.getValue();
    assertThat(savedPaths).hasSize(1);
    assertThat(savedPaths.get(0).getDivisionArea())
        .isEqualTo(DivisionArea.NON_SMOKING_ZONE);

    // Pending 데이터 정리 검증
    verify(validationRepository).deleteByPendingAddress(pendingAddress);
    verify(pendingPathRepository).deleteByPendingAddress(pendingAddress);
    verify(pendingAddressRepository).delete(pendingAddress);
  }

  @Test
  @DisplayName("여러 건의 승격 대상이 있으면 모두 처리된다")
  void shouldPromoteMultipleAddresses() throws Exception {
    // Given: 임계값을 충족하는 PendingAddress 2건
    PendingAddress address1 = PendingAddress.builder()
        .addressName("주소1")
        .buildingName("빌딩1")
        .addressPosLat(37.0)
        .addressPosLng(127.0)
        .addressCategory("공원")
        .paths(Collections.emptyList())
        .build();

    PendingAddress address2 = PendingAddress.builder()
        .addressName("주소2")
        .buildingName("빌딩2")
        .addressPosLat(38.0)
        .addressPosLng(128.0)
        .addressCategory("학교")
        .paths(Collections.emptyList())
        .build();

    given(pendingAddressRepository.findAddressesMeetingThreshold(10))
        .willReturn(List.of(address1, address2));

    given(addressRepository.save(any(Address.class)))
        .willReturn(Address.builder().id(1L).build());

    // When: Tasklet 실행
    RepeatStatus status = tasklet.execute(null, null);

    // Then: Address 2건 저장, Pending 2건 삭제
    assertThat(status).isEqualTo(RepeatStatus.FINISHED);
    verify(addressRepository, times(2)).save(any(Address.class));
    verify(pendingAddressRepository, times(2)).delete(any(PendingAddress.class));
  }
}
