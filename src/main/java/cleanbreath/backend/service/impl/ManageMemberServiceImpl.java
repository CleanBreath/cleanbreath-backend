package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.ResponseManageMemberDTO;
import cleanbreath.backend.entity.manage.ManageMember;
import cleanbreath.backend.service.JwtService;
import cleanbreath.backend.service.ManageMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManageMemberServiceImpl implements ManageMemberService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public Object verify(RequestManageMemberDTO account) {

        ManageMember manageRequest = account.toEntity();

        log.info("name = {}, password = {}", account.getUsername(), account.getPassword());


        Authentication authenticate
                = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                manageRequest.getUsername(),
                                manageRequest.getPassword())
                );

        if (authenticate.isAuthenticated()) {
            String generateToken = jwtService.generateToken(manageRequest.getUsername());

            return new ResponseManageMemberDTO(manageRequest, generateToken);
        }
        return new ResponseMessage(HttpStatus.FORBIDDEN, "아이디 및 비밀번호가 틀립니다.");
    }
}
