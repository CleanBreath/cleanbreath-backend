package cleanbreath.backend.controller;

import cleanbreath.backend.dto.PendingDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.service.PendingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// /v1/admin/** 는 WebConfig의 AdminTokenInterceptor가 보호한다
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/smokingArea")
public class AdminPendingAddressController {

    private final PendingAddressService pendingAddressService;

    // 투표 기준을 넘긴 승인 후보 (투표 수 포함)
    @GetMapping("/candidates")
    public ResponseEntity<List<PendingDto.DetailResponse>> getCandidates() {
        return ResponseEntity.ok(pendingAddressService.getPromotionCandidates());
    }

    // 요청 구역을 지도 데이터로 반영
    @PostMapping("/{id}/approve")
    public ResponseEntity<MessageResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(pendingAddressService.approve(id));
    }
}
