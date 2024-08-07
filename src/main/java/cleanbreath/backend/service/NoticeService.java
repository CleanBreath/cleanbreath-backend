package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.NoticeDTO.RequestNoticeDTO;
import cleanbreath.backend.dto.Manage.NoticeDTO.ResponseNoticeDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    Page<ResponseNoticeDTO> findAllNotice(Pageable pageable);
    ResponseNoticeDTO findNotice(Long id);
    ResponseMessage save(RequestNoticeDTO requestNoticeDTO, HttpSession session);
    ResponseMessage update(Long id ,RequestNoticeDTO requestNoticeDTO, HttpSession session);
    ResponseMessage delete(Long id, HttpSession session);
}
