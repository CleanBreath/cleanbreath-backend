package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.FeedbackDTO.RequestSaveFeedBackDTO;
import cleanbreath.backend.dto.FeedbackDTO.RequestUpdateFeedbackDTO;
import cleanbreath.backend.dto.FeedbackDTO.ResponseFeedbackDTO;
import cleanbreath.backend.dto.FeedbackDTO.ResponseListFeedbackDTO;

import java.util.List;

public interface FeedbackService {
    ResponseMessage save(RequestSaveFeedBackDTO feedBackDTO);
    List<ResponseListFeedbackDTO> findAllFeedback();
    ResponseFeedbackDTO findFeedback(Long id);
    ResponseMessage updateFeedBack(Long id, RequestUpdateFeedbackDTO updateDTO);
    ResponseMessage deleteFeedback(Long id);
}
