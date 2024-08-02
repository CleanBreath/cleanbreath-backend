package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.FeedbackDTO.*;
import cleanbreath.backend.entity.Feedback;
import cleanbreath.backend.repository.FeedbackRepository;
import cleanbreath.backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public ResponseMessage save(RequestSaveFeedBackDTO feedBackDTO) {
        if (!saveValidation(feedBackDTO)) {
            return new ResponseMessage(HttpStatus.NOT_FOUND, "피드백 저장 실패");
        }
        Feedback saveFeedback = feedBackDTO.toEntity();
        feedbackRepository.save(saveFeedback);
        return new ResponseMessage(HttpStatus.CREATED, "피드백 저장 성공");
    }

    public List<ResponseListFeedbackDTO> findAllFeedback() {
        List<Feedback> result = feedbackRepository.findAll();
        return result.stream().map(ResponseListFeedbackDTO::new).toList();
    }

    public Object findFeedback(Long id, RequestFeedbackAccountDTO accountDTO) {
        Optional<Feedback> userAccount = feedbackRepository
                .findByUsernameAndPassword(accountDTO.getUsername(), accountDTO.getPassword());

        if (userAccount.isEmpty()) {
            return new ResponseMessage(HttpStatus.UNAUTHORIZED, "아이디 및 비밀번호가 틀렸습니다.");
        }

        Feedback findFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드백은 없는 피드백입니다."));

        return new ResponseFeedbackDTO(findFeedback);
    }

    @Transactional
    public ResponseMessage updateFeedBack(Long id, RequestUpdateFeedbackDTO updateDTO) {
        Feedback findFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드백은 없습니다."));
        findFeedback.updateFeedback(
                updateDTO.getUpdateAt(),
                updateDTO.getTitle(),
                updateDTO.getContent()
        );
        return new ResponseMessage(HttpStatus.OK, "업데이트 성공");
    }

    @Transactional
    public ResponseMessage deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);

        return new ResponseMessage(HttpStatus.OK, "피드백 삭제 완료");
    }


    private boolean saveValidation(RequestSaveFeedBackDTO feedBackDTO) {
        if (feedBackDTO.getUsername().isEmpty() && feedBackDTO.getPassword().isEmpty()) {
            return false;
        }
        if (feedBackDTO.getTitle().isEmpty() && feedBackDTO.getContent().isEmpty()) {
            return false;
        }
        return true;
    }
}
