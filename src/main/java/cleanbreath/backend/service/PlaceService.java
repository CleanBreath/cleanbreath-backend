package cleanbreath.backend.service;

import cleanbreath.backend.dto.ResponseGetPlaceDTO;

import java.util.List;

public interface PlaceService {
    List<ResponseGetPlaceDTO> findPlacesWithinRadius(Double currentLat, Double currentLng);
}
