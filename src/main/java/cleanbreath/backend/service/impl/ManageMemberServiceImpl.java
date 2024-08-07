package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.entity.manage.ManageMember;
import cleanbreath.backend.repository.manage.ManageMemberRepository;
import cleanbreath.backend.service.ManageMemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManageMemberServiceImpl implements ManageMemberService {
    private final ManageMemberRepository manageMemberRepository;
    private final static String SESSION_KEY = "ManageMember";

    public ResponseMessage login(RequestManageMemberDTO loginForm, HttpSession session) {
        log.info("email = {} password = {}", loginForm.getEmail(), loginForm.getPassword());
        ManageMember loginMember = manageMemberRepository.findByEmailAndPassword(loginForm.getEmail(), loginForm.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("이메일 및 비밀번호가 맞지 않습니다."));

        session.setAttribute(SESSION_KEY, loginMember);
        return new ResponseMessage(HttpStatus.OK, "로그인 성공");
    }

    public ResponseMessage logout(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
        return new ResponseMessage(HttpStatus.OK, "로그아웃 성공");
    }
}
