package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.service.ManageMemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ManageMemberController {
    private final ManageMemberService manageMemberService;

    @PostMapping("/manage")
    public ResponseEntity<ResponseMessage> login(@RequestBody RequestManageMemberDTO loginForm, HttpSession session) {
        ResponseMessage login = manageMemberService.login(loginForm, session);
        return ResponseEntity.ok(login);
    }

    @GetMapping("/manage")
    public ResponseEntity<ResponseMessage> logout(HttpSession session) {
        ResponseMessage logout = manageMemberService.logout(session);
        return ResponseEntity.ok(logout);
    }
}
