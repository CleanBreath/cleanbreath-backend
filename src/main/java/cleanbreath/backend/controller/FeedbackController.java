package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.FeedbackDTO.*;
import cleanbreath.backend.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 전체 피드백 데이터 가져오기
    @GetMapping("/admin-v1/feedback-list")
    public ResponseEntity<List<ResponseListFeedbackDTO>> FeedbackList() {
        List<ResponseListFeedbackDTO> result = feedbackService.findAllFeedback();
        return ResponseEntity.ok(result);
    }

    // 사용자 피드백 추가하기
    @PostMapping("/v1/feedback/add")
    public ResponseEntity<ResponseMessage> addFeedback(@RequestBody RequestSaveFeedBackDTO saveDto) {
        ResponseMessage saveFeedback = feedbackService.save(saveDto);
        return ResponseEntity.ok(saveFeedback);
    }

    // 해당 피드백 가져오기
    @GetMapping("/admin-v1/admin/feedback/{id}")
    public ResponseEntity<ResponseFeedbackDTO> getFeedbackById(@PathVariable("id") Long id) {
        ResponseFeedbackDTO findFeedback = feedbackService.findFeedback(id);
        return ResponseEntity.ok(findFeedback);
    }

    // 해당 피드백 업데이트
    @PutMapping("/admin-v1/feedback/{id}")
    public ResponseEntity<ResponseMessage> updateFeedback(@PathVariable("id") Long id, @RequestBody RequestUpdateFeedbackDTO updateDTO) {
        ResponseMessage updateMessage = feedbackService.updateFeedBack(id, updateDTO);
        return ResponseEntity.ok(updateMessage);
    }

    // 해당 피드백 삭제
    @DeleteMapping("/admin-v1/feedback/{id}")
    public ResponseEntity<ResponseMessage> deleteFeedback(@PathVariable("id") Long id) {
        ResponseMessage deleteFeedback = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(deleteFeedback);
    }
}
