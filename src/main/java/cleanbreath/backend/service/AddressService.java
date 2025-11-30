package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    List<AddressDto.ListResponse> getAllAddresses();
    Page<AddressDto.ListResponse> getAllAddress(Pageable pageable);
    AddressDto.Response getAddress(Double lat, Double lng);
    Object updateAddress(AddressDto.CheckUpdate updateAtDTO);
}
