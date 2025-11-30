package cleanbreath.backend.service;

import cleanbreath.backend.dto.ApartmentDto;

import java.util.List;

public interface ApartmentService {
    List<ApartmentDto.Response> getAllApartments();
    List<ApartmentDto.Response> getRegionApartments(String region);
}
