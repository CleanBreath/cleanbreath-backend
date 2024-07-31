package cleanbreath.backend.entity;
/**
 * IMPLICIT : 암묵적,
 * OPEN : 개방형,
 * CLOSE : 폐쇠형,
 * LINE : 라인형,
 */
public enum DivisionArea {
    NON_SMOKING_ZONE, // 금연구역
    SMOKING_ZONE_OPEN_IMPLICIT,
    SMOKING_ZONE_OPEN,
    SMOKING_ZONE_CLOSE_IMPLICIT,
    SMOKING_ZONE_CLOSE,
    SMOKING_ZONE_LINE_IMPLICIT,
    SMOKING_ZONE_LINE
}
