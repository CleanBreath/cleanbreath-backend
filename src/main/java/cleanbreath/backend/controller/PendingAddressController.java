package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDto;
import cleanbreath.backend.dto.PendingDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.service.PendingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PendingAddressController {

    private final PendingAddressService pendingAddressService;

    @GetMapping("/allRequestAddress")
    public ResponseEntity<List<PendingDto.AddressResponse>> getAllRequestData(){
        List<PendingDto.AddressResponse> result = pendingAddressService.getAllManageAddress();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allRequestAddressPage")
    public ResponseEntity<PagedModel<PendingDto.AddressResponse>> getAllRequestDataPage(Pageable pageable){
        PagedModel<PendingDto.AddressResponse> result = pendingAddressService.getPageAllManageAddress(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/smokingArea/{id}")
    public ResponseEntity<PendingDto.DetailResponse> getAreaDetail(@PathVariable Long id) {
        PendingDto.DetailResponse result = pendingAddressService.getAddressDetail(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/smokingArea/{id}/vote")
    public ResponseEntity<MessageResponse> voteArea(
            @PathVariable Long id,
            @RequestBody PendingDto.ValidationVoteRequest request) {
        MessageResponse message = pendingAddressService.vote(id, request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/smokingArea/add")
    public ResponseEntity<MessageResponse> addSmokingArea(@RequestBody AddressDto.Request requestSmokingArea) {
        MessageResponse message = pendingAddressService.saveAddressData(requestSmokingArea);
        return ResponseEntity.ok(message);
    }
}
