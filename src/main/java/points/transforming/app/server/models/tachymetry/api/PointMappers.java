package points.transforming.app.server.models.tachymetry.api;

import points.transforming.app.server.models.tachymetry.polarmethod.PointDto;

public final class PointMappers {

    public static PointReportResponse toPointResponse(final PointDto point) {
        return PointReportResponse.builder()
            .name(point.getName())
            .angle(point.getAngle())
            .distance(point.getDistance())
            .calculatedPicket(PicketResponse.of(point.getCalculatedPicket()))
            .build();
    }
}
