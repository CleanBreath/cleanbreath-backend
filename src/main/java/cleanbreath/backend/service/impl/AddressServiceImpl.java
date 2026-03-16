package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.common.ApiResponse;
import cleanbreath.backend.dto.common.BaseResponse;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AddressServiceImpl implements AddressService {
    private static final long UPDATE_THRESHOLD_DAYS = 30;
    
    private final AddressRepository addressRepository;

    public List<AddressDto.Response> getAllAddresses() {
        List<Address> findAddressList = addressRepository.findAllWithPaths();
        return findAddressList.stream()
                .map(AddressDto.Response::new)
                .toList();
    }

    public AddressDto.Response getAddress(Double lat, Double lng) {
        Address findAddress = addressRepository.findByAddressPosLatAndAddressPosLng(lat, lng)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
        return new AddressDto.Response(findAddress);
    }

    public BaseResponse updateAddress(AddressDto.CheckUpdate updateAtDTO) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime checkingUpdateAt = updateAtDTO.getUpdateDate();
        long daysBetween = ChronoUnit.DAYS.between(checkingUpdateAt, currentDate);

        if (daysBetween < UPDATE_THRESHOLD_DAYS) {
            return MessageResponse.of("아직 업데이트 시기가 아닙니다.");
        }
        
        List<Address> result = addressRepository.findAllWithPaths();
        List<AddressDto.Response> convertResult = result.stream()
                .map(AddressDto.Response::new)
                .toList();
        return ApiResponse.of(convertResult.size(), currentDate, convertResult);
    }
}
