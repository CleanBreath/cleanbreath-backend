package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PublicNoSmokingZoneDto;
import cleanbreath.backend.dto.common.ApiResponse;
import cleanbreath.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AddressController {

    private final AddressService addressService;

    /**
     * 금연 및 흡연구역의 모든 정보를 반환한다.
     * @apiNote '/v1/allAddress'
     */
    @GetMapping("/allAddress")
    public ResponseEntity<?> getAllAddresses() {
        List<AddressDto.Response> result = addressService.getAllAddresses();
        LocalDateTime updateAt = LocalDateTime.now();

        return ResponseEntity.ok(ApiResponse.of(result.size(), updateAt, result));
    }

    /**
     * 위도 및 경도 값을 받아서 일치하는 주소를 반환한다.
     * @apiNote '/v1/address?lat=37.235634&lng=126.34264645'
     */
    @GetMapping("/address")
    public ResponseEntity<AddressDto.Response> getAddress(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        AddressDto.Response findAddress = addressService.getAddress(lat, lng);
        return ResponseEntity.ok(findAddress);
    }

    /**
     * 대표 좌표가 지도 화면 사각형(남서·북동 모서리) 안에 있는 구역을 반환한다.
     * @apiNote '/v1/address/within?swLat=37.38&swLng=126.95&neLat=37.40&neLng=126.97'
     */
    @GetMapping("/address/within")
    public ResponseEntity<?> getAddressesWithin(@RequestParam("swLat") double swLat, @RequestParam("swLng") double swLng,
                                                @RequestParam("neLat") double neLat, @RequestParam("neLng") double neLng) {
        List<AddressDto.Response> result = addressService.getAddressesWithin(swLat, swLng, neLat, neLng);
        return ResponseEntity.ok(ApiResponse.of(result.size(), LocalDateTime.now(), result));
    }

    /**
     * 구역 다각형이 이 좌표를 포함하는 구역을 반환한다. (지금 이 위치가 금연·흡연 구역인지)
     * @apiNote '/v1/address/contains?lat=37.3928&lng=126.9637'
     */
    @GetMapping("/address/contains")
    public ResponseEntity<?> getAddressesContaining(@RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        List<AddressDto.Response> result = addressService.getAddressesContaining(lat, lng);
        return ResponseEntity.ok(ApiResponse.of(result.size(), LocalDateTime.now(), result));
    }

    /**
     * 지도 화면 사각형 안의 공공데이터 실외 금연구역(점 좌표)을 반환한다. 최대 3000건.
     * @apiNote '/v1/publicZone/within?swLat=37.55&swLng=126.97&neLat=37.57&neLng=126.99'
     */
    @GetMapping("/publicZone/within")
    public ResponseEntity<?> getPublicZonesWithin(@RequestParam("swLat") double swLat, @RequestParam("swLng") double swLng,
                                                  @RequestParam("neLat") double neLat, @RequestParam("neLng") double neLng) {
        List<PublicNoSmokingZoneDto.Response> result = addressService.getPublicZonesWithin(swLat, swLng, neLat, neLng);
        return ResponseEntity.ok(ApiResponse.of(result.size(), LocalDateTime.now(), result));
    }

    /**
     * 날짜 값을 받아 30일 지나면 최신데이터를 다시 반환하고, 지나지 않으면 업데이트 시기가 아니라고 메시지 반환한다.
     * @apiNote '/v1/updateDate
     */
    @PostMapping("/updateDate")
    public ResponseEntity<?> updateAddress(@Valid @RequestBody AddressDto.CheckUpdate updateAtDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(updateAtDTO));
    }
}
