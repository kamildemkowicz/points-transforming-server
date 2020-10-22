package points.transforming.app.server.models.tachymetry.api;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.tachymetry.TachymetryPicketMeasured;

@Getter
@Builder
public class PointDto {
    private final String picketInternalId;
    private final BigDecimal angle;
    private final BigDecimal distance;

    public static PointDto of(final TachymetryPicketMeasured tachymetryPicketMeasured) {
        return PointDto.builder()
            .picketInternalId(tachymetryPicketMeasured.getPicketInternalId())
            .distance(BigDecimal.valueOf(tachymetryPicketMeasured.getDistance()))
            .angle(BigDecimal.valueOf(tachymetryPicketMeasured.getAngle()))
            .build();
    }
}
