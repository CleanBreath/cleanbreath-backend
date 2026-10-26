package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PendingDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.entity.pending.AreaValidationRequest;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.repository.pending.SmokingAreaValidateRepository;
import cleanbreath.backend.service.PendingAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PendingAddressServiceImpl implements PendingAddressService {

    private final PendingAddressRepository addressRepository;
    private final PendingPathRepository pathRepository;
    private final SmokingAreaValidateRepository validationRepository;
    private final AddressRepository mapAddressRepository;
    private final PathRepository mapPathRepository;

    // 관리자 승인 후보로 보여줄 최소 사실 투표 수
    @Value("${promotion.threshold}")
    private int promotionThreshold;

    public List<PendingDto.AddressResponse> getAllManageAddress() {
        return addressRepository.findAllWithPaths()
                .stream()
                .map(PendingDto.AddressResponse::new)
                .toList();
    }

    public PagedModel<PendingDto.AddressResponse> getPageAllManageAddress(Pageable pageable) {
        Page<PendingDto.AddressResponse> page = addressRepository.findAll(pageable)
                .map(PendingDto.AddressResponse::new);
        return new PagedModel<>(page);
    }

    public PendingDto.AddressResponse getManageAddressById(Long id) {
        return addressRepository.findById(id)
                .map(PendingDto.AddressResponse::new)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_ADDRESS_NOT_FOUND));
    }

    public PendingDto.DetailResponse getAddressDetail(Long id) {
        PendingAddress pendingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_ADDRESS_NOT_FOUND));

        int truthCount = validationRepository.sumTruthByPendingAddressId(id);
        int untruthCount = validationRepository.sumUntruthByPendingAddressId(id);

        return new PendingDto.DetailResponse(pendingAddress, truthCount, untruthCount);
    }

    @Transactional
    public MessageResponse vote(Long addressId, PendingDto.ValidationVoteRequest request) {
        // 중복 투표 방지 (client_token + target_id UNIQUE)
        if (validationRepository.existsByClientTokenAndTargetId(
                request.getClientToken(), addressId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_VALIDATION_REQUEST);
        }

        PendingAddress pendingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_ADDRESS_NOT_FOUND));

        AreaValidationRequest validationRequest = AreaValidationRequest.builder()
                .pendingAddress(pendingAddress)
                .clientToken(request.getClientToken())
                .targetId(addressId)
                .truth(request.isTruth() ? 1 : 0)
                .untruth(request.isTruth() ? 0 : 1)
                .build();

        validationRepository.save(validationRequest);

        return MessageResponse.of("투표가 완료되었습니다.");
    }

    @Transactional
    public MessageResponse saveAddressData(AddressDto.Request addressDTO) {
        validateSaveRequest(addressDTO);

        PendingAddress savedAddress = addressRepository.save(addressDTO.toEntity());

        // N번 세이브 대신에 saveAll() 한 번에 처리
        List<PendingPath> pendingPaths = addressDTO.getPaths().stream()
                .map(path -> {
                    Path.Coordinates cleaned = Path.removeNearDuplicatePoints(path.getPathLat(), path.getPathLng());
                    return PendingPath.builder()
                            .divisionArea(path.getDivisionArea())
                            .pathLat(cleaned.pathLat())
                            .pathLng(cleaned.pathLng())
                            .pendingAddress(savedAddress)
                            .build();
                })
                .toList();

        pathRepository.saveAll(pendingPaths);

        return MessageResponse.of("주소 및 영역 저장 성공");
    }

    @Transactional
    public MessageResponse updateAddressData(Long id, AddressDto.Update addressDTO) {
        PendingAddress findPendingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_ADDRESS_NOT_FOUND));

        PendingPath findPendingPath = pathRepository.findByPendingAddress(findPendingAddress)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_PATH_NOT_FOUND));

        findPendingAddress.updateManageAddress(
                addressDTO.getAddressName(),
                addressDTO.getBuildingName(),
                addressDTO.getLatitude(),
                addressDTO.getLongitude(),
                addressDTO.getCategory()
        );

        return MessageResponse.of("업데이트 성공");
    }

    @Transactional
    public MessageResponse deleteAddressData(Long id) {
        PendingAddress findPendingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_ADDRESS_NOT_FOUND));

        // 엔티티 전달하여 타입 불일치 문제 해결
        pathRepository.deleteByPendingAddress(findPendingAddress);
        addressRepository.delete(findPendingAddress);

        return MessageResponse.of("해당 주소 및 영역 삭제 성공");
    }

    public List<PendingDto.DetailResponse> getPromotionCandidates() {
        return addressRepository.findAddressesMeetingThreshold(promotionThreshold)
                .stream()
                .map(pending -> new PendingDto.DetailResponse(pending,
                        validationRepository.sumTruthByPendingAddressId(pending.getId()),
                        validationRepository.sumUntruthByPendingAddressId(pending.getId())))
                .toList();
    }

    // 투표는 익명이라 조작될 수 있으므로, 지도 반영은 관리자가 승인한 요청만 한다
    @Transactional
    public MessageResponse approve(Long id) {
        PendingAddress pending = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENDING_ADDRESS_NOT_FOUND));

        Address savedAddress = mapAddressRepository.save(Address.builder()
                .updateAt(LocalDateTime.now())
                .addressName(pending.getAddressName())
                .buildingName(pending.getBuildingName())
                .addressPosLat(pending.getAddressPosLat())
                .addressPosLng(pending.getAddressPosLng())
                .addressCategory(pending.getAddressCategory())
                .build());

        // DivisionArea 기준으로 금연/흡연 구역 구분
        mapPathRepository.saveAll(pending.getPaths().stream()
                .map(pendingPath -> Path.builder()
                        .address(savedAddress)
                        .divisionArea(pendingPath.getDivisionArea())
                        .pathLat(pendingPath.getPathLat())
                        .pathLng(pendingPath.getPathLng())
                        .build())
                .toList());

        validationRepository.deleteByPendingAddress(pending);
        pathRepository.deleteByPendingAddress(pending);
        addressRepository.delete(pending);

        log.info("[승인] 요청 {} → 구역 {} 반영", id, savedAddress.getId());
        return MessageResponse.of("승인되어 지도에 반영되었습니다.");
    }

    private void validateSaveRequest(AddressDto.Request address) {
        if (address.getAddressName().isBlank() || address.getBuildingName().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (address.getLatitude() == null || address.getLongitude() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (address.getUpdateAt() == null || address.getCategory().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (address.getPaths() == null || address.getPaths().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        // 승격 배치가 다각형으로 바꾸다 실패하지 않도록 등록 시점에 검사한다
        for (AddressDto.PathRequest path : address.getPaths()) {
            try {
                Path.toPolygonWkt(path.getPathLat(), path.getPathLng());
                Path.Coordinates cleaned = Path.removeNearDuplicatePoints(path.getPathLat(), path.getPathLng());
                Path.toPolygonWkt(cleaned.pathLat(), cleaned.pathLng());
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.INVALID_INPUT);
            }
        }
    }
}
