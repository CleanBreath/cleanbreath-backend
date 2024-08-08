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

    /**
     * 안양시 공동주택 금연구역 지정 현황 및 위치 정보 가져온다.
     * @apiNote "server.bluesky-cleanbreath.com/v1/apartment
     */
    @GetMapping("/apartment")
    public ResponseEntity<List<ResponseAllApartmentDTO>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }

    /**
     * 지역 검색하여 해당 지역의 공동주택 금연구역 지정 현황 및 위치 정보 가져온다.
     * @apiNote "server.bluesky-cleanbreath.com/v1/region?r=동안구"
     */
    @GetMapping("/region")
    public ResponseEntity<List<ResponseRegionApartmentDTO>> getRegionsApartments(@RequestParam("r") String region) {
        return ResponseEntity.ok(apartmentService.getRegionApartments(region));
    }
}
