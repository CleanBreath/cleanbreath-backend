package cleanbreath.backend.entity.pending;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "area_validation_request", indexes = {
    // 외래키 인덱스 - PendingAddress 연관 조회 최적화
    @Index(name = "idx_area_validation_address_id", columnList = "m_address_id")
})
public class AreaValidationRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "m_address_id")
    private PendingAddress pendingAddress;

    private int truth; // 흡연구역 판별 숫자
    private int untruth; // 금연구역 판별 숫자
}
