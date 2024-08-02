package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.FeedbackDTO.RequestFeedbackAccountDTO;
import cleanbreath.backend.dto.FeedbackDTO.RequestSaveFeedBackDTO;
import cleanbreath.backend.dto.FeedbackDTO.RequestUpdateFeedbackDTO;
import cleanbreath.backend.dto.FeedbackDTO.ResponseListFeedbackDTO;
import cleanbreath.backend.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    ResponseMessage save(RequestSaveFeedBackDTO feedBackDTO);
    List<ResponseListFeedbackDTO> findAllFeedback();
    Object findFeedback(Long id, RequestFeedbackAccountDTO accountDTO);
    ResponseMessage updateFeedBack(Long id, RequestUpdateFeedbackDTO updateDTO);
    ResponseMessage deleteFeedback(Long id);
}
