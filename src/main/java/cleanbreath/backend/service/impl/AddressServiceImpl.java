package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PublicNoSmokingZoneDto;
import cleanbreath.backend.dto.common.ApiResponse;
import cleanbreath.backend.dto.common.BaseResponse;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PublicNoSmokingZoneRepository;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AddressServiceImpl implements AddressService {
    private static final long UPDATE_THRESHOLD_DAYS = 30;
    // 화면이 넓어도 응답이 커지지 않게 막는다. 프론트는 확대했을 때만 부른다
    private static final int PUBLIC_ZONE_LIMIT = 3000;
    
    private final AddressRepository addressRepository;
    private final PublicNoSmokingZoneRepository publicNoSmokingZoneRepository;

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

    public List<AddressDto.Response> getAddressesWithin(double swLat, double swLng, double neLat, double neLng) {
        validateCoordinate(swLat, swLng);
        validateCoordinate(neLat, neLng);
        return toResponses(addressRepository.findIdsWithin(swLat, swLng, neLat, neLng));
    }

    public List<AddressDto.Response> getAddressesContaining(double lat, double lng) {
        validateCoordinate(lat, lng);
        return toResponses(addressRepository.findIdsContaining(lat, lng));
    }

    public List<PublicNoSmokingZoneDto.Response> getPublicZonesWithin(double swLat, double swLng, double neLat, double neLng) {
        validateCoordinate(swLat, swLng);
        validateCoordinate(neLat, neLng);
        return publicNoSmokingZoneRepository.findWithin(swLat, swLng, neLat, neLng, PUBLIC_ZONE_LIMIT).stream()
                .map(PublicNoSmokingZoneDto.Response::new)
                .toList();
    }

    private List<AddressDto.Response> toResponses(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return addressRepository.findAllWithPathsByIdIn(ids).stream()
                .map(AddressDto.Response::new)
                .toList();
    }

    // NaN도 여기서 걸러진다 (비교가 모두 false)
    private void validateCoordinate(double lat, double lng) {
        if (!(lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
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
