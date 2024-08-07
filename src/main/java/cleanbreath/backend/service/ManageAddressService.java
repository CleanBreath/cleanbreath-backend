package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.AddressDTO.ResponseManageAddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManageAddressService {
    List<ResponseManageAddressDTO> getAllManageAddress();
    Page<ResponseManageAddressDTO> GetPageAllManageAddress(Pageable pageable);
    ResponseManageAddressDTO getManageAddressById(Long id);
    ResponseMessage saveAddressData(RequestAddressDTO addressDTO);
}
