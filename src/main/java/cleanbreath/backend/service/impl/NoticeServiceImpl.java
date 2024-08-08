package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.NoticeDTO.RequestNoticeDTO;
import cleanbreath.backend.dto.Manage.NoticeDTO.ResponseNoticeDTO;
import cleanbreath.backend.entity.manage.Notice;
import cleanbreath.backend.repository.manage.NoticeRepository;
import cleanbreath.backend.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final static String SESSION_KEY = "ManageMember";


    public Page<ResponseNoticeDTO> findAllNotice(Pageable pageable) {
        Page<Notice> findAllNotice = noticeRepository.findAll(pageable);
        return findAllNotice.map(ResponseNoticeDTO::new);
    }

    public ResponseNoticeDTO findNotice(Long id) {
        Notice findNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항은 존재하지 않습니다."));

        return new ResponseNoticeDTO(findNotice);
    }

    @Transactional
    public ResponseMessage save(RequestNoticeDTO requestNoticeDTO, HttpSession session) {
        Object sessionLogin = session.getAttribute(SESSION_KEY);
        if (sessionLogin == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN, "로그인 필요한 기능입니다.");
        }

        Notice saveNotice = requestNoticeDTO.toEntity();
        noticeRepository.save(saveNotice);

        return new ResponseMessage(HttpStatus.OK, "공지사항 저장 성공");
    }

    @Transactional
    public ResponseMessage update(Long id ,RequestNoticeDTO requestNoticeDTO, HttpSession session) {
        Object sessionLogin = session.getAttribute(SESSION_KEY);
        if (sessionLogin == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN, "로그인 필요한 기능입니다.");
        }

        Notice updateNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항은 존재하지 않습니다."));

        updateNotice.updateNotice(requestNoticeDTO.getTitle(), requestNoticeDTO.getContent());
        return new ResponseMessage(HttpStatus.OK, "공지사항 업데이트 성공");
    }

    @Transactional
    public ResponseMessage delete(Long id, HttpSession session) {
        Object sessionLogin = session.getAttribute(SESSION_KEY);
        if (sessionLogin == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN, "로그인 필요한 기능입니다.");
        }

        noticeRepository.deleteById(id);
        return new ResponseMessage(HttpStatus.OK, "공지사항 삭제 성공");
    }
}
