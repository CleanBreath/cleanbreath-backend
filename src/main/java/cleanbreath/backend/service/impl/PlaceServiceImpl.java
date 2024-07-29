package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.ResponseGetPlaceDTO;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.service.PlaceService;
import cleanbreath.backend.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {
    private final PathRepository pathRepository;

    private static final double RADIUS = 150; // 반경 50M

    public List<ResponseGetPlaceDTO> findPlacesWithinRadius(Double currentLat, Double currentLng) {
        List<Path> findAllPaths = pathRepository.findAll();
        return findAllPaths.stream()
                .filter(path -> {
                    double pathLat = parseLatitude(path.getPathLat());
                    double pathLng = parseLatitude(path.getPathLng());
                    double distance = DistanceUtil.calculateDistance(currentLat, currentLng, pathLat, pathLng);
                    return distance <= RADIUS;
                })
                .map(path -> {
                    Address address = path.getAddress();
                    return new ResponseGetPlaceDTO(
                            address.getId(),
                            address.getAddressName(),
                            address.getBuildingName(),
                            address.getAddressPosLat(),
                            address.getAddressPosLng(),
                            path.getDivisionArea(),
                            path.getPathLat(),
                            path.getPathLng()
                    );
                }).toList();
    }

    private double parseLatitude(String latStr) {
        return Double.parseDouble(latStr.split(",")[0]);
    }
    private double parseLongitude(String lngStr) {
        return Double.parseDouble(lngStr.split(",")[0]);
    }

}
