package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PublicNoSmokingZoneDto;
import cleanbreath.backend.dto.common.ApiResponse;
import cleanbreath.backend.dto.common.BaseResponse;

import java.util.List;

public interface AddressService {
    List<AddressDto.Response> getAllAddresses();
    AddressDto.Response getAddress(Double lat, Double lng);
    List<AddressDto.Response> getAddressesWithin(double swLat, double swLng, double neLat, double neLng);
    List<AddressDto.Response> getAddressesContaining(double lat, double lng);
    List<PublicNoSmokingZoneDto.Response> getPublicZonesWithin(double swLat, double swLng, double neLat, double neLng);
    BaseResponse updateAddress(AddressDto.CheckUpdate updateAtDTO);
}
