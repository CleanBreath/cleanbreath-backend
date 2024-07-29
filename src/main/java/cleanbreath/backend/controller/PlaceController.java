package cleanbreath.backend.controller;

import cleanbreath.backend.dto.ResponseGetPlaceDTO;
import cleanbreath.backend.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/v1/find-50m-place")
    public List<ResponseGetPlaceDTO> getPlaceWithRadius(@RequestParam("currentLat") double currentLat, @RequestParam("currentLng") double currentLng) {
        return placeService.findPlacesWithinRadius(currentLat, currentLng);
    }
}
