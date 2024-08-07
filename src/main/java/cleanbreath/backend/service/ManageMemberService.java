package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import jakarta.servlet.http.HttpSession;

public interface ManageMemberService {
    ResponseMessage login(RequestManageMemberDTO loginForm, HttpSession session);
    ResponseMessage logout(HttpSession session);
}
