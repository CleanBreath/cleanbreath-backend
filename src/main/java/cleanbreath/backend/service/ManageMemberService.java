package cleanbreath.backend.service;

import cleanbreath.backend.dto.Manage.ManageMemberDTO.RequestManageMemberDTO;
import cleanbreath.backend.dto.Manage.ManageMemberDTO.ResponseManageMemberDTO;

public interface ManageMemberService {
    Object verify(RequestManageMemberDTO account);
}
