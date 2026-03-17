package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PendingDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.pending.AreaValidationRequest;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.repository.pending.SmokingAreaValidateRepository;
import cleanbreath.backend.service.PendingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PendingAddressServiceImpl implements PendingAddressService {

    private final PendingAddressRepository addressRepository;
    private final PendingPathRepository pathRepository;
    private final SmokingAreaValidateRepository validationRepository;

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
                .map(path -> PendingPath.builder()
                        .divisionArea(path.getDivisionArea())
                        .pathLat(path.getPathLat())
                        .pathLng(path.getPathLng())
                        .pendingAddress(savedAddress)
                        .build())
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
    }
}
