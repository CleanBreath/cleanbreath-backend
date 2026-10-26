package cleanbreath.backend.dto;

import cleanbreath.backend.entity.PublicNoSmokingZone;

import java.time.LocalDate;

public class PublicNoSmokingZoneDto {

    public record Response(Long id, String name, String zoneType, String division, String scope,
                           String roadAddress, Double latitude, Double longitude, LocalDate referenceDate) {
        public Response(PublicNoSmokingZone zone) {
            this(zone.getId(), zone.getName(), zone.getZoneType(), zone.getDivision(), zone.getScope(),
                    zone.getRoadAddress(), zone.getLat(), zone.getLng(), zone.getReferenceDate());
        }
    }
}
