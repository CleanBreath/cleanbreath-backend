package cleanbreath.backend.controller;

import cleanbreath.backend.dto.FeedbackDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 전체 피드백 데이터 가져오기
    @GetMapping("/feedback-list")
    public ResponseEntity<List<FeedbackDto.ListResponse>> getFeedbackList() {
        List<FeedbackDto.ListResponse> result = feedbackService.findAllFeedback();
        return ResponseEntity.ok(result);
    }

    // 사용자 피드백 추가하기
    @PostMapping("/feedback/add")
    public ResponseEntity<MessageResponse> addFeedback(@RequestBody FeedbackDto.Create saveDto) {
        MessageResponse saveFeedback = feedbackService.save(saveDto);
        return ResponseEntity.ok(saveFeedback);
    }

    // 해당 피드백 가져오기
    @GetMapping("/feedback/{id}")
    public ResponseEntity<FeedbackDto.Response> getFeedbackById(@PathVariable("id") Long id) {
        FeedbackDto.Response findFeedback = feedbackService.findFeedback(id);
        return ResponseEntity.ok(findFeedback);
    }

    // 해당 피드백 업데이트
    @PutMapping("/feedback/{id}")
    public ResponseEntity<MessageResponse> updateFeedback(@PathVariable("id") Long id, @RequestBody FeedbackDto.Update updateDTO) {
        MessageResponse updateMessage = feedbackService.updateFeedBack(id, updateDTO);
        return ResponseEntity.ok(updateMessage);
    }

    // 해당 피드백 삭제
    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<MessageResponse> deleteFeedback(@PathVariable("id") Long id) {
        MessageResponse deleteFeedback = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(deleteFeedback);
    }
}
