package cleanbreath.backend.dto.Manage.ManageMemberDTO;

import cleanbreath.backend.entity.manage.ManageMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestManageMemberDTO {
    private String email;
    private String password;

    public ManageMember toEntity() {
        return ManageMember.builder()
                .email(email)
                .password(password)
                .build();
    }
}
