package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.ApartmentDto;
import cleanbreath.backend.entity.Apartment;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
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
    public List<ApartmentDto.Response> getAllApartments() {
        return apartmentRepository.findAll()
                .stream()
                .map(ApartmentDto.Response::new)
                .toList();
    }

    /**
     * 지역이름을 받아서 해당 지역 모든 아파트들을 찾는다.
     * 위와 마찬가지로 엔티티를 DTO로 변환하여 반환한다.
     */
    public List<ApartmentDto.Response> getRegionApartments(String region) {
        List<ApartmentDto.Response> result = apartmentRepository.findByRegion(region)
                .stream()
                .map(ApartmentDto.Response::new)
                .toList();

        if (result.isEmpty()) {
            throw new BusinessException(ErrorCode.REGION_NOT_FOUND);
        }

        return result;
    }
}
