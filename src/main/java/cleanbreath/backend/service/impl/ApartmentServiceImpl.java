package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.ApartmentDTO.ResponseAllApartmentDTO;
import cleanbreath.backend.dto.ApartmentDTO.ResponseRegionApartmentDTO;
import cleanbreath.backend.entity.Apartment;
import cleanbreath.backend.repository.ApartmentRepository;
import cleanbreath.backend.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;

    /**
     * 모든 금연구역 지정된 모든 아파트를 찾는다.
     * stream과 mapd을 사용해 엔티티를 DTO로 변환해서 반환한다.
     */
    public List<ResponseAllApartmentDTO> getAllApartments() {
        List<Apartment> result = apartmentRepository.findAll();

        return result.stream().map(ResponseAllApartmentDTO::new).toList();
    }

    /**
     * 지역이름을 받아서 해당 지역 모든 아파트들을 찾는다.
     * 위와 마찬가지로 엔티티를 DTO로 변환하여 반환한다.
     */
    public List<ResponseRegionApartmentDTO> getRegionApartments(String region) {
        List<Apartment> result = apartmentRepository.findByRegion(region);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("해당 지역은 없는 지역입니다.");
        }

        return result.stream().map(ResponseRegionApartmentDTO::new).toList();
    }
}
