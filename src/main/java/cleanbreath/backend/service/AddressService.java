package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    List<ResponseAddressDTO> getAllAddresses();
    Page<ResponseAddressDTO> getAllAddress(Pageable pageable);
    ResponseAddressDTO getAddress(Long addressId);
    void deleteAddress(Long addressId);
    Address saveAddress(RequestAddressDTO requestAddressDTO);
}
