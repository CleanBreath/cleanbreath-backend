package cleanbreath.backend.controller;

import cleanbreath.backend.dto.ApartmentDTO.ResponseAllApartmentDTO;
import cleanbreath.backend.dto.ApartmentDTO.ResponseRegionApartmentDTO;
import cleanbreath.backend.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ApartmentController {
    private final ApartmentService apartmentService;

    @GetMapping("/apartment")
    public ResponseEntity<List<ResponseAllApartmentDTO>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }

    @GetMapping("/region")
    public ResponseEntity<List<ResponseRegionApartmentDTO>> getRegionsApartments(@RequestParam("r") String region) {
        return ResponseEntity.ok(apartmentService.getRegionApartments(region));
    }
}
