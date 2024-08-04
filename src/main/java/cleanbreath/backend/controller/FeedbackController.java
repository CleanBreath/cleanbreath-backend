package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.FeedbackDTO.RequestFeedbackAccountDTO;
import cleanbreath.backend.dto.FeedbackDTO.RequestSaveFeedBackDTO;
import cleanbreath.backend.dto.FeedbackDTO.RequestUpdateFeedbackDTO;
import cleanbreath.backend.dto.FeedbackDTO.ResponseListFeedbackDTO;
import cleanbreath.backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 전체 피드백 데이터 가져오기
    @GetMapping("/v1/feedback-list")
    public ResponseEntity<List<ResponseListFeedbackDTO>> FeedbackList() {
        List<ResponseListFeedbackDTO> result = feedbackService.findAllFeedback();
        return ResponseEntity.ok(result);
    }

    // 피드백 추가하기
    @PostMapping("/v1/feedback/add")
    public ResponseEntity<ResponseMessage> addFeedback(@RequestBody RequestSaveFeedBackDTO saveDto) {
        ResponseMessage saveFeedback = feedbackService.save(saveDto);
        return ResponseEntity.ok(saveFeedback);
    }

    // 해당 피드백 가져오기
    @GetMapping("/v1/feedback/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable("id") Long id, @RequestBody RequestFeedbackAccountDTO accountDTO) {
        Object findFeedback = feedbackService.findFeedback(id, accountDTO);
        return ResponseEntity.ok(findFeedback);
    }

    // 해당 피드백 업데이트
    @PutMapping("/v1/feedback/{id}")
    public ResponseEntity<ResponseMessage> updateFeedback(@PathVariable("id") Long id, @RequestBody RequestUpdateFeedbackDTO updateDTO) {
        ResponseMessage updateMessage = feedbackService.updateFeedBack(id, updateDTO);
        return ResponseEntity.ok(updateMessage);
    }

    // 해당 피드백 삭제
    @DeleteMapping("/v1/feedback/{id}")
    public ResponseEntity<ResponseMessage> deleteFeedback(@PathVariable("id") Long id) {
        ResponseMessage deleteFeedback = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(deleteFeedback);
    }
}
