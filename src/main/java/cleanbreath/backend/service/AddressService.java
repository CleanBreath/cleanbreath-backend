package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.*;
import cleanbreath.backend.dto.PathDTO.RequestPathDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    List<ResponseAllAddressDTO> getAllAddresses();
    Page<ResponseAllAddressDTO> getAllAddress(Pageable pageable);
    ResponseAddressDTO getAddress(Double lat, Double lng);
    ResponseSaveMessage saveAddress(RequestAddressDTO address);
    ResponseDeleteAddressDTO deleteAddress(Long addressId);
}
