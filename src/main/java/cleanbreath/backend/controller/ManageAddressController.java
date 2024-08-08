package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.RequestAddressDTO;
import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.AddressDTO.ResponseManageAddressDTO;
import cleanbreath.backend.service.ManageAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ManageAddressController {

    private final ManageAddressService manageAddressService;

    @GetMapping("/allRequestData")
    public ResponseEntity<List<ResponseManageAddressDTO>> getAllRequestData(){
        List<ResponseManageAddressDTO> result = manageAddressService.getAllManageAddress();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allRequestDataPage")
    public ResponseEntity<Page<ResponseManageAddressDTO>> getAllRequestDataPage(Pageable pageable){
        Page<ResponseManageAddressDTO> result = manageAddressService.GetPageAllManageAddress(pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/smokingArea/add")
    public ResponseEntity<ResponseMessage> addSmokingArea(@RequestBody RequestAddressDTO requestSmokingArea) {
        ResponseMessage message = manageAddressService.saveAddressData(requestSmokingArea);
        return ResponseEntity.ok(message);
    }
}
