package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto.Response> getAllAddresses();
    AddressDto.Response getAddress(Double lat, Double lng);
    Object updateAddress(AddressDto.CheckUpdate updateAtDTO);
}
