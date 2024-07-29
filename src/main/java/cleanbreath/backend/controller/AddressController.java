package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAllAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseSaveMessage;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/v1/allData")
    public ResponseEntity<List<ResponseAllAddressDTO>> getAllAddresses() {
        List<ResponseAllAddressDTO> allAddresses = addressService.getAllAddresses();
        return ResponseEntity.ok(allAddresses);
    }

    @GetMapping("/v1/data")
    public ResponseEntity<ResponseAddressDTO> getAddress(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        ResponseAddressDTO findAddress = addressService.getAddress(lat, lng);
        return ResponseEntity.ok(findAddress);
    }

    @PostMapping("/v1/saveData")
    public ResponseEntity<ResponseSaveMessage> saveAddress(@RequestBody RequestAddressDTO address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(address));
    }
}
