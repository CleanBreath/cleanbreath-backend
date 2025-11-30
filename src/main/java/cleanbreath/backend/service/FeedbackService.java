package cleanbreath.backend.service;

import cleanbreath.backend.dto.FeedbackDto;
import cleanbreath.backend.dto.common.MessageResponse;

import java.util.List;

public interface FeedbackService {
    MessageResponse save(FeedbackDto.Create feedBackDTO);
    List<FeedbackDto.ListResponse> findAllFeedback();
    FeedbackDto.Response findFeedback(Long id);
    MessageResponse updateFeedBack(Long id, FeedbackDto.Update updateDTO);
    MessageResponse deleteFeedback(Long id);
}
