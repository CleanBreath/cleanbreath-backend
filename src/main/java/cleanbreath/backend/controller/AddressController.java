package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.RequestCheckUpdateAtDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAllAddressDTO;
import cleanbreath.backend.dto.ResponseAllDataUpdateCheckDTO;
import cleanbreath.backend.service.AddressService;
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
     * @apiNote 'server.bluesky-cleanbreath.com/v1/allData'
     */
    @GetMapping("/allData")
    public ResponseEntity<?> getAllAddresses() {
        List<ResponseAllAddressDTO> result = addressService.getAllAddresses();
        LocalDateTime updateAt = LocalDateTime.now();

        return ResponseEntity.ok(new ResponseAllDataUpdateCheckDTO<>(result.size(), updateAt, result));
    }

    /**
     * 위도 및 경도 값을 받아서 일치하는 주소를 반환한다.
     * @apiNote 'server.bluesky-cleanbreath.com/v1/data?lat=37.235634&lng=126.34264645'
     */
    @GetMapping("/data")
    public ResponseEntity<ResponseAddressDTO> getAddress(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        ResponseAddressDTO findAddress = addressService.getAddress(lat, lng);
        return ResponseEntity.ok(findAddress);
    }

    /**
     * 날짜 값을 받아 30일 지나면 최신데이터를 다시 반환하고, 지나지 않으면 업데이트 시기가 아니라고 메시지 반환한다.
     * @apiNote 'server.bluesky-cleanbreath.com/v1/updateDate
     */
    @PostMapping("/updateDate")
    public ResponseEntity<?> updateAddress(@RequestBody RequestCheckUpdateAtDTO updateAtDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(updateAtDTO));
    }
}
