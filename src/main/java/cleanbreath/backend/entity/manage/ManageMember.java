package cleanbreath.backend.entity.manage;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ManageMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "m_name")
    private String name;
    @Column(nullable = false, name = "m_email")
    private String email;
    @Column(nullable = false, name = "m_password")
    private String password;
    @Column(nullable = false, name = "m_phone")
    private String phone;

    @Column(nullable = false, name = "m_role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
