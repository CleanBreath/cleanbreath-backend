package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.ResponseManageMemberDTO;
import cleanbreath.backend.entity.manage.ManageMember;
import cleanbreath.backend.repository.manage.ManageMemberRepository;
import cleanbreath.backend.service.ManageMemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManageMemberServiceImpl implements ManageMemberService {
    private final ManageMemberRepository manageMemberRepository;
    private final static String SESSION_KEY = "ManageMember";

    public ResponseManageMemberDTO login(RequestManageMemberDTO loginForm, HttpServletRequest request) {
        log.info("email = {} password = {}", loginForm.getEmail(), loginForm.getPassword());
        ManageMember loginMember = manageMemberRepository.findByEmailAndPassword(loginForm.getEmail(), loginForm.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("이메일 및 비밀번호가 맞지 않습니다."));


        HttpSession session = request.getSession(true);
        String[] sessionData = {loginMember.getName(), loginMember.getEmail()};
        session.setAttribute(SESSION_KEY, sessionData);

        return new ResponseManageMemberDTO(loginMember);
    }

    public ResponseMessage logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseMessage(HttpStatus.OK, "로그아웃 성공");
    }
}
