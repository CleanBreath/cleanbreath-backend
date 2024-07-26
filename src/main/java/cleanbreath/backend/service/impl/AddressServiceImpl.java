package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseAddressDTO;
import cleanbreath.backend.entity.Address;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public List<ResponseAddressDTO> getAllAddresses() {
        List<Address> findAddressList = addressRepository.findAll();

        return findAddressList.stream()
                .map(ResponseAddressDTO::new)
                .toList();

    }

    public Page<ResponseAddressDTO> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable)
                .map(ResponseAddressDTO::new);
    }

    public ResponseAddressDTO getAddress(Long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new IllegalArgumentException("This Address ID doses not exist")
        );
        return new ResponseAddressDTO(findAddress);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Transactional
    public Address saveAddress(RequestAddressDTO requestAddressDTO) {
        if (!validateAddress(requestAddressDTO)) {
            throw new IllegalArgumentException("Invalid Address");
        }
        Address address = Address.builder()
                .updateAt(requestAddressDTO.getUpdateAt())
                .addressName(requestAddressDTO.getAddressName())
                .buildingName(requestAddressDTO.getBuildingName())
                .addressPosLat(requestAddressDTO.getLatitude())
                .addressPosLng(requestAddressDTO.getLongitude())
                .addressCategory(requestAddressDTO.getCategory())
                .build();

        return addressRepository.save(address);
    }


    private boolean validateAddress(RequestAddressDTO requestAddressDTO) {
        if (requestAddressDTO.getAddressName().isEmpty()) {
            return false;
        }
        if (requestAddressDTO.getBuildingName().isEmpty()) {
            return false;
        }
        if (null == requestAddressDTO.getLatitude()) {
            return false;
        }
        if (null == requestAddressDTO.getLongitude()) {
            return false;
        }
        if (requestAddressDTO.getCategory().isEmpty()) {
            return false;
        }
        return true;
    }
}
