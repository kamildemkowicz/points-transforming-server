package points.transforming.app.server.models.tachymetry.api;

import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.tachymetry.polarmethod.Point;

public final class PointMappers {
    public static Point toPoint(final GeodeticControlNetworkPointRequest pointRequest, final Measurement measurement) {
        final var point = new Point();
        point.setName(pointRequest.getName());
        point.setCoordinateX(pointRequest.getCoordinateX());
        point.setCoordinateY(pointRequest.getCoordinateY());
        point.setMeasurement(measurement);

        return point;
    }

    public static PointResponse toPointResponse(final Point point) {
        return PointResponse.builder()
            .name(point.getName())
            .coordinateX(point.getCoordinateX())
            .coordinateY(point.getCoordinateY())
            .build();
    }
}
