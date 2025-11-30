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

    /**
     * 요청 받은 전체 데이터를 가져온다.
     */
    public List<PendingDto.AddressResponse> getAllManageAddress() {
        List<PendingAddress> result = addressRepository.findAll();
        return result.stream()
                .map(PendingDto.AddressResponse::new)
                .toList();
    }

    /**
     * 요청 받은 전체 데이터를 가져온다.(페이징 시스템 추가)
     */
    public PagedModel<PendingDto.AddressResponse> GetPageAllManageAddress(
            Pageable pageable
    ) {
        Page<PendingDto.AddressResponse> list = addressRepository.findAll(pageable).map(PendingDto.AddressResponse::new);
        return new PagedModel<>(list);
    }

    /**
     * 아이디 값을 받아서 해당 주소 정보를 가져온다.
     */
    public PendingDto.AddressResponse getManageAddressById(Long id) {
        return addressRepository.findById(id).map(PendingDto.AddressResponse::new).orElse(null);
    }

    /**
     * 흡연구역의 주소 및 영역을 저장한다.
     * Client -> Pending Address
     */
    @Transactional
    public MessageResponse saveAddressData(AddressDto.Request addressDTO) {
        if (!saveAddressValidate(addressDTO)) {
             return MessageResponse.of("주소 및 영역 저장실패");
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

    /**
     * 흡연구역 및 장소 영역을 업데이트 한다.
     * Client -> Pending Address
     */
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
    /**
     * 해당 주소 아이디를 받아 주소 및 영역 동시 삭제
     * Client -> Pending Address
     */
    @Transactional
    public MessageResponse deleteAddressDTO(Long id) {
        addressRepository.deleteById(id);
        pathRepository.deleteByPendingAddress(id);
        return MessageResponse.of("해당 주소 및 영역 삭제 성공");
    }

    // Save Address ValidateCheck
    private boolean saveAddressValidate(AddressDto.Request address) {
        if (address.getAddressName().isEmpty() && address.getBuildingName().isEmpty()) {
            return false;
        }
        if (address.getLatitude().isNaN() && address.getLongitude().isNaN()) {
            return false;
        }
        return address.getUpdateAt() != null || !address.getCategory().isEmpty();
    }
}
