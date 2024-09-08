package cleanbreath.backend.controller;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.ResponseManageMemberDTO;
import cleanbreath.backend.service.ManageMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ManageMemberController {
    private final ManageMemberService manageMemberService;

    @PostMapping("/verify")
    public Object login(@RequestBody RequestManageMemberDTO request) {
        return manageMemberService.verify(request);
    }
}
