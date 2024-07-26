package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAllAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseDeleteAddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    List<ResponseAllAddressDTO> getAllAddresses();
    Page<ResponseAllAddressDTO> getAllAddress(Pageable pageable);
    ResponseAddressDTO getAddress(Double lat, Double lng);
    ResponseDeleteAddressDTO deleteAddress(Long addressId);
}
