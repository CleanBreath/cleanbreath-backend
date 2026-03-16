package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.FeedbackDto;
import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.entity.Feedback;
import cleanbreath.backend.exception.BusinessException;
import cleanbreath.backend.exception.ErrorCode;
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
        if (feedBackDTO.getTitle().isBlank() || feedBackDTO.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        feedbackRepository.save(feedBackDTO.toEntity());
        return MessageResponse.of("피드백 저장 성공");
    }

    public List<FeedbackDto.ListResponse> findAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(FeedbackDto.ListResponse::new)
                .toList();
    }

    public FeedbackDto.Response findFeedback(Long id) {
        Feedback findFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND));
        return new FeedbackDto.Response(findFeedback);
    }

    @Transactional
    public MessageResponse updateFeedBack(Long id, FeedbackDto.Update updateDTO) {
        Feedback findFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND));

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
            throw new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND);
        }

        feedbackRepository.deleteById(id);
        return MessageResponse.of("피드백 삭제 완료");
    }
}
