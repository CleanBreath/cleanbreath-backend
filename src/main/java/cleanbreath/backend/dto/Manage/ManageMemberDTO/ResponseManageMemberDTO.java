package cleanbreath.backend.dto.Manage.ManageMemberDTO;

import cleanbreath.backend.entity.manage.ManageMember;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseManageMemberDTO {
    private String email;
    private String name;

    public ResponseManageMemberDTO(ManageMember manageMember) {
        this.email = manageMember.getEmail();
        this.name = manageMember.getName();
    }
}
