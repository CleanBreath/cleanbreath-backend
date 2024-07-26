package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.service.AddressService;
import cleanbreath.backend.service.PathService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/api/allData")
    public ResponseEntity<List<ResponseAddressDTO>> getAllAddresses() {
        List<ResponseAddressDTO> allAddresses = addressService.getAllAddresses();
        return ResponseEntity.ok(allAddresses);
    }
}
