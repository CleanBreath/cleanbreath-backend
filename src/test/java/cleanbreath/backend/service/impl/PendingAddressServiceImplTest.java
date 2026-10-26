package cleanbreath.backend.service.impl;

import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * 구역 요청 관리자 승인 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class PendingAddressServiceImplTest {

  @Mock private PendingAddressRepository addressRepository;
  @Mock private PendingPathRepository pathRepository;
  @Mock private SmokingAreaValidateRepository validationRepository;
  @Mock private AddressRepository mapAddressRepository;
  @Mock private PathRepository mapPathRepository;

  @InjectMocks
  private PendingAddressServiceImpl service;

  @Test
  @DisplayName("승인하면 요청 구역이 지도 데이터로 옮겨지고 요청·투표 기록은 삭제된다")
  void approve_promotesAndCleansUp() {
    PendingPath pendingPath = PendingPath.builder()
        .divisionArea(DivisionArea.NON_SMOKING_ZONE)
        .pathLat("37.123")
        .pathLng("127.456")
        .build();
    PendingAddress pending = PendingAddress.builder()
        .addressName("테스트 주소")
        .buildingName("테스트 빌딩")
        .addressPosLat(37.5665)
        .addressPosLng(126.9780)
        .addressCategory("공원")
        .paths(List.of(pendingPath))
        .build();
    given(addressRepository.findById(1L)).willReturn(Optional.of(pending));
    given(mapAddressRepository.save(any(Address.class))).willReturn(Address.builder().id(9L).build());

    service.approve(1L);

    ArgumentCaptor<Address> address = ArgumentCaptor.forClass(Address.class);
    verify(mapAddressRepository).save(address.capture());
    assertThat(address.getValue().getAddressName()).isEqualTo("테스트 주소");
    assertThat(address.getValue().getAddressPosLat()).isEqualTo(37.5665);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Path>> paths = ArgumentCaptor.forClass(List.class);
    verify(mapPathRepository).saveAll(paths.capture());
    assertThat(paths.getValue()).singleElement()
        .satisfies(p -> assertThat(p.getDivisionArea()).isEqualTo(DivisionArea.NON_SMOKING_ZONE));

    verify(validationRepository).deleteByPendingAddress(pending);
    verify(pathRepository).deleteByPendingAddress(pending);
    verify(addressRepository).delete(pending);
  }

  @Test
  @DisplayName("없는 요청을 승인하면 404 예외이고 아무것도 저장하지 않는다")
  void approve_unknownId_throws() {
    given(addressRepository.findById(1L)).willReturn(Optional.empty());

    assertThatThrownBy(() -> service.approve(1L)).isInstanceOf(BusinessException.class);
    verify(mapAddressRepository, never()).save(any());
  }

  @Test
  @DisplayName("승인 후보는 설정한 임계값으로 조회하고 투표 수를 함께 돌려준다")
  void candidates_useThresholdAndCounts() {
    ReflectionTestUtils.setField(service, "promotionThreshold", 20);
    PendingAddress pending = PendingAddress.builder().id(3L).addressName("후보").paths(List.of()).build();
    given(addressRepository.findAddressesMeetingThreshold(20)).willReturn(List.of(pending));
    given(validationRepository.sumTruthByPendingAddressId(3L)).willReturn(25);
    given(validationRepository.sumUntruthByPendingAddressId(3L)).willReturn(4);

    assertThat(service.getPromotionCandidates()).singleElement().satisfies(c -> {
      assertThat(c.getId()).isEqualTo(3L);
      assertThat(c.getTruthCount()).isEqualTo(25);
      assertThat(c.getUntruthCount()).isEqualTo(4);
    });
  }
}
