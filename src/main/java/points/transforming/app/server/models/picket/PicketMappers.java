package points.transforming.app.server.models.picket;

import points.transforming.app.server.models.tachymetry.polarmethod.Point;

public final class PicketMappers {
    public static Picket toPicket(final Point point) {
        final var picket = new Picket();

        picket.setName(point.getName());
        picket.setCoordinateX(point.getCoordinateX().doubleValue());
        picket.setCoordinateY(point.getCoordinateY().doubleValue());
        picket.setMeasurement(point.getMeasurement());

        return picket;
    }
}
