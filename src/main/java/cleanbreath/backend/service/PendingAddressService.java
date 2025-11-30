package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Pending.AddressDTO.ResponsePendingAddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PendingAddressService {
    List<ResponsePendingAddressDTO> getAllManageAddress();
    Page<ResponsePendingAddressDTO> GetPageAllManageAddress(Pageable pageable);
    ResponsePendingAddressDTO getManageAddressById(Long id);
    ResponseMessage saveAddressData(RequestAddressDTO addressDTO);
}
