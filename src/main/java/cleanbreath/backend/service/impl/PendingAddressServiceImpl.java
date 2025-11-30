package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.RequestUpdateAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.PathDTO.RequestPathDTO;
import cleanbreath.backend.dto.Pending.AddressDTO.ResponsePendingAddressDTO;
import cleanbreath.backend.entity.pending.PendingAddress;
import cleanbreath.backend.entity.pending.PendingPath;
import cleanbreath.backend.repository.pending.PendingAddressRepository;
import cleanbreath.backend.repository.pending.PendingPathRepository;
import cleanbreath.backend.service.PendingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public List<ResponsePendingAddressDTO> getAllManageAddress() {
        List<PendingAddress> result = addressRepository.findAll();
        return result.stream()
                .map(ResponsePendingAddressDTO::new)
                .toList();
    }

    /**
     * 요청 받은 전체 데이터를 가져온다.(페이징 시스템 추가)
     */
    public Page<ResponsePendingAddressDTO> GetPageAllManageAddress(Pageable pageable) {
        return addressRepository.findAll(pageable).map(ResponsePendingAddressDTO::new);
    }

    /**
     * 아이디 값을 받아서 해당 주소 정보를 가져온다.
     */
    public ResponsePendingAddressDTO getManageAddressById(Long id) {
        return addressRepository.findById(id).map(ResponsePendingAddressDTO::new).orElse(null);
    }

    /**
     * 흡연구역의 주소 및 영역을 저장한다.
     * Client -> Pending Address
     */
    @Transactional
    public ResponseMessage saveAddressData(RequestAddressDTO addressDTO) {
        if (!saveAddressValidate(addressDTO)) {
             return new ResponseMessage(HttpStatus.NOT_FOUND, "주소 및 영역 저장실패");
        }
        PendingAddress saveAddress = addressDTO.toEntity();
        addressRepository.save(saveAddress);

        for (RequestPathDTO path : addressDTO.getPaths()) {
            PendingPath savePath = PendingPath.builder()
                    .divisionArea(path.getDivisionArea())
                    .pathLat(path.getPathLat())
                    .pathLng(path.getPathLng())
                    .pendingAddress(saveAddress)
                    .build();

            pathRepository.save(savePath);
        }

        return new ResponseMessage(HttpStatus.CREATED, "주소 및 영역 저장 성공");
    }

    /**
     * 흡연구역 및 장소 영역을 업데이트 한다.
     * Client -> Pending Address
     */
    @Transactional
    public ResponseMessage updateAddressData(Long id, RequestUpdateAddressDTO addressDTO) {
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

        findPendingPath.updatePath(
                addressDTO.getPaths().getFirst().getDivisionArea(),
                addressDTO.getPaths().getFirst().getPathLat(),
                addressDTO.getPaths().getFirst().getPathLng()
        );

        return new ResponseMessage(HttpStatus.OK, "업데이트 성공");
    }
    /**
     * 해당 주소 아이디를 받아 주소 및 영역 동시 삭제
     * Client -> Pending Address
     */
    @Transactional
    public ResponseMessage deleteAddressDTO(Long id) {
        addressRepository.deleteById(id);
        pathRepository.deleteByPendingAddress(id);
        return new ResponseMessage(HttpStatus.OK, "해당 주소 및 영역 삭제 성공");
    }

    // Save Address ValidateCheck
    private boolean saveAddressValidate(RequestAddressDTO address) {
        if (address.getAddressName().isEmpty() && address.getBuildingName().isEmpty()) {
            return false;
        }
        if (address.getLatitude().isNaN() && address.getLongitude().isNaN()) {
            return false;
        }
        return address.getUpdateAt() != null || !address.getCategory().isEmpty();
    }
}
