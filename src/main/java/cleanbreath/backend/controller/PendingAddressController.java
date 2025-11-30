package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Pending.AddressDTO.ResponsePendingAddressDTO;
import cleanbreath.backend.service.PendingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PendingAddressController {

    private final PendingAddressService pendingAddressService;

    @GetMapping("/allRequestAddress")
    public ResponseEntity<List<ResponsePendingAddressDTO>> getAllRequestData(){
        List<ResponsePendingAddressDTO> result = pendingAddressService.getAllManageAddress();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allRequestAddressPage")
    public ResponseEntity<Page<ResponsePendingAddressDTO>> getAllRequestDataPage(Pageable pageable){
        Page<ResponsePendingAddressDTO> result = pendingAddressService.GetPageAllManageAddress(pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/smokingArea/add")
    public ResponseEntity<ResponseMessage> addSmokingArea(@RequestBody RequestAddressDTO requestSmokingArea) {
        ResponseMessage message = pendingAddressService.saveAddressData(requestSmokingArea);
        return ResponseEntity.ok(message);
    }
}
