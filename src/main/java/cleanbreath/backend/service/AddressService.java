package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.common.ApiResponse;
import cleanbreath.backend.dto.common.BaseResponse;

import java.util.List;

public interface AddressService {
    List<AddressDto.Response> getAllAddresses();
    AddressDto.Response getAddress(Double lat, Double lng);
    BaseResponse updateAddress(AddressDto.CheckUpdate updateAtDTO);
}
