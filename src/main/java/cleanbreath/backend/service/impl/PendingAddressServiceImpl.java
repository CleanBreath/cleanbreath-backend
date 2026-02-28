package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PendingDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
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

    public List<PendingDto.AddressResponse> getAllManageAddress() {
        List<PendingAddress> result = addressRepository.findAllWithPaths();
        return result.stream()
                .map(PendingDto.AddressResponse::new)
                .toList();
    }

    public PagedModel<PendingDto.AddressResponse> getPageAllManageAddress(Pageable pageable) {
        Page<PendingDto.AddressResponse> list = addressRepository.findAll(pageable)
                .map(PendingDto.AddressResponse::new);
        return new PagedModel<>(list);
    }

    @Transactional
    public MessageResponse saveAddressData(AddressDto.Request addressDTO) {
        if (!saveAddressValidate(addressDTO)) {
             return MessageResponse.of("주소 및 영역 저장 실패");
        }
        PendingAddress saveAddress = addressDTO.toEntity();
        addressRepository.save(saveAddress);

        for (AddressDto.PathRequest path : addressDTO.getPaths()) {
            PendingPath savePath = PendingPath.builder()
                    .divisionArea(path.getDivisionArea())
                    .pathLat(path.getPathLat())
                    .pathLng(path.getPathLng())
                    .pendingAddress(saveAddress)
                    .build();
            pathRepository.save(savePath);
        }

        return MessageResponse.of("주소 및 영역 저장 성공");
    }

    @Transactional
    public MessageResponse updateAddressData(Long id, AddressDto.Update addressDTO) {
        PendingAddress findPendingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 장소는 없습니다."));

        PendingPath findPendingPath = pathRepository.findByPendingAddress(findPendingAddress)
                .orElseThrow(() -> new IllegalArgumentException("해당 영역은 없습니다."));

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
    public MessageResponse deleteAddressDTO(Long id) {
        PendingAddress pendingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 존재하지 않습니다."));
        pathRepository.deleteByPendingAddress(pendingAddress);
        addressRepository.deleteById(id);
        return MessageResponse.of("해당 주소 및 영역 삭제 성공");
    }

    private boolean saveAddressValidate(AddressDto.Request address) {
        return address.getAddressName() != null && !address.getAddressName().isEmpty()
            && address.getBuildingName() != null && !address.getBuildingName().isEmpty()
            && address.getLatitude() != null && !address.getLatitude().isNaN()
            && address.getLongitude() != null && !address.getLongitude().isNaN()
            && address.getUpdateAt() != null
            && address.getCategory() != null;
    }
}
