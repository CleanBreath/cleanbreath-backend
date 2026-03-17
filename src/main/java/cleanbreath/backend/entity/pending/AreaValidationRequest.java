package cleanbreath.backend.entity.pending;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "area_validation_request",
    uniqueConstraints = {
        // 동일 클라이언트가 같은 대상에 중복 투표 방지
        @UniqueConstraint(name = "uk_client_token_target_id", columnNames = {"client_token", "target_id"})
    },
    indexes = {
        // 외래키 인덱스 - PendingAddress 연관 조회 최적화
        @Index(name = "idx_area_validation_address_id", columnList = "m_address_id")
    }
)
public class AreaValidationRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "m_address_id")
    private PendingAddress pendingAddress;

    // 프론트엔드에서 생성한 랜덤 토큰 (중복 투표 방지용)
    @Column(name = "client_token", nullable = false)
    private String clientToken;

    // 투표 대상 ID
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    private int truth; // 판별 숫자
    private int untruth; // 판별 숫자
}
