package points.transforming.app.server.services.measurement;

import java.util.stream.Collectors;

import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.MeasurementRequest;
import points.transforming.app.server.services.picket.PicketMappers;

public final class MeasurementMapper {

    public static Measurement toMeasurementFrom(final MeasurementRequest measurementRequest) {
        var result = new Measurement();
        result.setName(measurementRequest.getName());
        result.setPlace(measurementRequest.getPlace());
        result.setOwner(measurementRequest.getOwner());
        result.setPickets(measurementRequest.getPickets()
            .stream()
            .map(p -> PicketMappers.toPicketFrom(p, result))
            .collect(Collectors.toList()));

        return result;
    }
}
