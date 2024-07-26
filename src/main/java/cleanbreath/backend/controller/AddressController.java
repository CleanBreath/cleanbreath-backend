package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAllAddressDTO;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
