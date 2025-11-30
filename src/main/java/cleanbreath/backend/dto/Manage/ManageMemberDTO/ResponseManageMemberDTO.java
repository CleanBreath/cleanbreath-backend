package cleanbreath.backend.dto.Manage.ManageMemberDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseManageMemberDTO {
    private String username;
    private String token;

    public ResponseManageMemberDTO(ManageMember manageMember, String token) {
        this.username = manageMember.getUsername();
        this.token = token;
    }
}
