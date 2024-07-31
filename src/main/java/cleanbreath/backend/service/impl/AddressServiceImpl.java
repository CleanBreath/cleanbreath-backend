package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.*;
import cleanbreath.backend.dto.PathDTO.RequestPathDTO;
import cleanbreath.backend.dto.ResponseAllDataUpdateCheckDTO;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) @Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final PathRepository pathRepository;

    // 전체 데이터 가져오기
    public List<ResponseAllAddressDTO> getAllAddresses() {
        List<Address> findAddressList = addressRepository.findAll();

        return findAddressList.stream()
                .map(ResponseAllAddressDTO::new)
                .toList();
    }

    // 전체 데이터 가져오기 (페이징 시스템 추가된 버전)
    public Page<ResponseAllAddressDTO> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable)
                .map(ResponseAllAddressDTO::new);
    }

    // 좌표값을 입력받아 해당 주소 가져오기
    public ResponseAddressDTO getAddress(Double lat, Double lng) {
        Address findAddress = addressRepository.findByAddressPosLatAndAddressPosLng(lat, lng)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return new ResponseAddressDTO(findAddress);
    }

    // 주소 및 영역 저장
    @Transactional
    public ResponseMessage saveAddress(RequestAddressDTO address) {
        if (!saveAddressValid(address)) {
            return new ResponseMessage(HttpStatus.NOT_FOUND, "주소 및 영역 저장실패");
        }

        Address saveAddress = address.toEntity();
        addressRepository.save(saveAddress);

        for (RequestPathDTO path : address.getPaths()) {
            Path savePath = Path.builder()
                    .divisionArea(path.getDivisionArea())
                    .pathLat(path.getPathLat())
                    .pathLng(path.getPathLng())
                    .address(saveAddress)
                    .build();

            pathRepository.save(savePath);
        }

        return new ResponseMessage(HttpStatus.CREATED, "주소 저장성공");
    }

    // 주소 삭제
    @Transactional
    public ResponseDeleteAddressDTO deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);

        return new ResponseDeleteAddressDTO(HttpStatus.OK, "해당 주소 삭제 성공");
    }

    // 주소 데이터 업데이트
    public Object updateAddress(RequestCheckUpdateAtDTO updateAtDTO) {
        List<Address> result = addressRepository.findAll();
        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime checkingUpdateAt = updateAtDTO.getUpdateDate();
        long daysBetween = ChronoUnit.DAYS.between(currentDate, checkingUpdateAt);

        if (daysBetween >= 30) {
            return new ResponseAllDataUpdateCheckDTO<>(result.size(), currentDate, result);
        } else {
            return new ResponseMessage(HttpStatus.NOT_MODIFIED, "아직 업데이트 시기가 아닙니다.");
        }
    }


    // Save Address Validation Check
    private boolean saveAddressValid(RequestAddressDTO address) {
        if (address.getAddressName().isEmpty() || address.getBuildingName().isEmpty()) {
            return false;
        }
        if (address.getLatitude().isNaN() || address.getLongitude().isNaN()) {
            return false;
        }
        if (address.getUpdateAt() == null || address.getCategory().isEmpty()) {
            return false;
        }
        return true;
    }
}
