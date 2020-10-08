package points.transforming.app.server.models.tachymetry.api;

import points.transforming.app.server.models.tachymetry.polarmethod.PointDto;

public final class PointMappers {

    public static PointResponse toPointResponse(final PointDto point) {
        return PointResponse.builder()
            .name(point.getName())
            .angle(point.getAngle())
            .distance(point.getDistance())
            .calculatedPicket(point.getCalculatedPicket())
            .build();
    }
}
