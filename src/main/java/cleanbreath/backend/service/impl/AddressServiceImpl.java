package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.common.ApiResponse;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<AddressDto.ListResponse> getAllAddresses() {
        List<Address> findAddressList = addressRepository.findAll();

        return findAddressList.stream()
                .map(AddressDto.ListResponse::new)
                .toList();
    }

    // 전체 데이터 가져오기 (페이징 시스템 추가된 버전)
    public Page<AddressDto.ListResponse> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable)
                .map(AddressDto.ListResponse::new);
    }

    // 좌표값을 입력받아 해당 주소 가져오기
    public AddressDto.Response getAddress(Double lat, Double lng) {
        Address findAddress = addressRepository.findByAddressPosLatAndAddressPosLng(lat, lng)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return new AddressDto.Response(findAddress);
    }

    // 주소 데이터 업데이트
    public Object updateAddress(AddressDto.CheckUpdate updateAtDTO) {
        List<Address> result = addressRepository.findAll();
        List<AddressDto.Response> convertResult = result.stream()
                .map(AddressDto.Response::new)
                .toList();

        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime checkingUpdateAt = updateAtDTO.getUpdateDate();
        long daysBetween = ChronoUnit.DAYS.between(currentDate, checkingUpdateAt);

        if (daysBetween >= 30) {
            return ApiResponse.of(convertResult.size(), currentDate, convertResult);
        } else {
            return MessageResponse.of("아직 업데이트 시기가 아닙니다.");
        }
    }
}
