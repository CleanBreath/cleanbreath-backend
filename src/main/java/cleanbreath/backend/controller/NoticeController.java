package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.NoticeDTO.RequestNoticeDTO;
import cleanbreath.backend.dto.Manage.NoticeDTO.ResponseNoticeDTO;
import cleanbreath.backend.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/findAllNotice")
    public ResponseEntity<Page<ResponseNoticeDTO>> findAllNotice(Pageable pageable) {
        Page<ResponseNoticeDTO> results = noticeService.findAllNotice(pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/notice/{id}")
    public ResponseEntity<ResponseNoticeDTO> findNotice(@PathVariable("id") Long id) {
        ResponseNoticeDTO results = noticeService.findNotice(id);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/admin-v1/notice/add")
    public ResponseEntity<ResponseMessage> save(@RequestBody RequestNoticeDTO dto, HttpSession session) {
        ResponseMessage results = noticeService.save(dto, session);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/admin-v1/notice/{id}")
    public ResponseEntity<ResponseMessage> update(@PathVariable("id") Long id, @RequestBody RequestNoticeDTO dto, HttpSession session) {
        ResponseMessage results = noticeService.update(id, dto, session);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/admin-v1/notice/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable("id") Long id, HttpSession session) {
        ResponseMessage results = noticeService.delete(id, session);
        return ResponseEntity.ok(results);
    }
}
