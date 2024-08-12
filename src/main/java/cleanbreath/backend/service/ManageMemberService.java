package cleanbreath.backend.service;

import cleanbreath.backend.dto.AddressDTO.ResponseMessage;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.ResponseManageMemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface ManageMemberService {
    ResponseManageMemberDTO login(RequestManageMemberDTO loginForm, HttpServletRequest request);
    ResponseMessage logout(HttpServletRequest request, HttpServletResponse response);
}
