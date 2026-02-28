package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.FeedbackDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.Feedback;
import cleanbreath.backend.repository.FeedbackRepository;
import cleanbreath.backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public MessageResponse save(FeedbackDto.Create feedBackDTO) {
        if (!saveValidation(feedBackDTO)) {
            return MessageResponse.of("피드백 저장 실패");
        }
        Feedback saveFeedback = feedBackDTO.toEntity();
        feedbackRepository.save(saveFeedback);
        return MessageResponse.of("피드백 저장 성공");
    }

    public List<FeedbackDto.ListResponse> findAllFeedback() {
        List<Feedback> result = feedbackRepository.findAll();
        return result.stream().map(FeedbackDto.ListResponse::new).toList();
    }

    public FeedbackDto.Response findFeedback(Long id) {
        Feedback findFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드백은 존재하지 않습니다."));
        return new FeedbackDto.Response(findFeedback);
    }

    @Transactional
    public MessageResponse updateFeedBack(Long id, FeedbackDto.Update updateDTO) {
        Feedback findFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드백은 없습니다."));
        findFeedback.updateFeedback(
                updateDTO.getUpdateAt(),
                updateDTO.getTitle(),
                updateDTO.getContent()
        );
        return MessageResponse.of("업데이트 성공");
    }

    @Transactional
    public MessageResponse deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 피드백은 존재하지 않습니다.");
        }
        feedbackRepository.deleteById(id);
        return MessageResponse.of("피드백 삭제 완료");
    }

    private boolean saveValidation(FeedbackDto.Create feedBackDTO) {
        return feedBackDTO.getTitle() != null && !feedBackDTO.getTitle().isEmpty() 
            && feedBackDTO.getContent() != null && !feedBackDTO.getContent().isEmpty();
    }
}
