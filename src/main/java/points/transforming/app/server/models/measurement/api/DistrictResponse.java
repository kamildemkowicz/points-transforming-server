package points.transforming.app.server.models.measurement.api;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.measurement.District;

@Getter
@Builder
public class DistrictResponse {
    private final Integer id;
    private final String name;
    private final Integer zone;

    public static DistrictResponse of(final District district) {
        return DistrictResponse.builder()
            .id(district.getId())
            .name(district.getName())
            .zone(district.getZone())
            .build();
    }
}
