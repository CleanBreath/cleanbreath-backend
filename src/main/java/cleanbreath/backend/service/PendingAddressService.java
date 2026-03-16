package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PendingDto;
import cleanbreath.backend.dto.common.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface PendingAddressService {

    List<PendingDto.AddressResponse> getAllManageAddress();

    PagedModel<PendingDto.AddressResponse> getPageAllManageAddress(Pageable pageable);

    PendingDto.AddressResponse getManageAddressById(Long id);

    MessageResponse saveAddressData(AddressDto.Request addressDTO);

    MessageResponse updateAddressData(Long id, AddressDto.Update addressDTO);

    MessageResponse deleteAddressData(Long id);
}
