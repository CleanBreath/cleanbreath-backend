package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.*;
import cleanbreath.backend.dto.ResponseAllDataUpdateCheckDTO;
import cleanbreath.backend.service.AddressService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/v1/allData")
    public ResponseEntity<?> getAllAddresses() {
        List<ResponseAllAddressDTO> result = addressService.getAllAddresses();
        LocalDateTime updateAt = LocalDateTime.now();

        return ResponseEntity.ok(new ResponseAllDataUpdateCheckDTO<>(result.size(), updateAt, result));
    }

    @GetMapping("/v1/data")
    public ResponseEntity<ResponseAddressDTO> getAddress(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        ResponseAddressDTO findAddress = addressService.getAddress(lat, lng);
        return ResponseEntity.ok(findAddress);
    }

//    @PostMapping("/v1/saveData")
    public ResponseEntity<ResponseMessage> saveAddress(@RequestBody RequestAddressDTO address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(address));
    }

    @GetMapping("/v1/updateDate")
    public ResponseEntity<?> updateAddress(@RequestBody RequestCheckUpdateAtDTO updateAtDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(updateAtDTO));
    }
}
