package cleanbreath.backend.service;

import cleanbreath.backend.dto.ApartmentDTO.ResponseAllApartmentDTO;
import cleanbreath.backend.dto.ApartmentDTO.ResponseRegionApartmentDTO;

import java.util.List;

public interface ApartmentService {
    List<ResponseAllApartmentDTO> getAllApartments();
    List<ResponseRegionApartmentDTO> getRegionApartments(String region);
}
