package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.*;
import cleanbreath.backend.dto.ResponseAllDataUpdateCheckDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface AddressService {
    List<ResponseAllAddressDTO> getAllAddresses();
    Page<ResponseAllAddressDTO> getAllAddress(Pageable pageable);
    ResponseAddressDTO getAddress(Double lat, Double lng);
    ResponseMessage saveAddress(RequestAddressDTO address);
    ResponseDeleteAddressDTO deleteAddress(Long addressId);
    Object updateAddress(RequestCheckUpdateAtDTO updateAtDTO);
}
