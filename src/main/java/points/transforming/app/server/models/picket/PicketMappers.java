package points.transforming.app.server.models.picket;

import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.tachymetry.api.GeodeticControlNetworkPointRequest;

public final class PicketMappers {
    public static Picket toPicket(final GeodeticControlNetworkPointRequest controlNetworkPointRequest, final Measurement measurement,
                                  final String picketInternalId) {
        final var picket = new Picket();

        picket.setName(controlNetworkPointRequest.getName());
        picket.setCoordinateX2000(controlNetworkPointRequest.getCoordinateX().doubleValue());
        picket.setCoordinateY2000(controlNetworkPointRequest.getCoordinateY().doubleValue());
        picket.setMeasurement(measurement);
        picket.setPicketInternalId(picketInternalId);

        return picket;
    }
}
