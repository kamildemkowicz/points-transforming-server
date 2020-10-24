package points.transforming.app.server.services.measurement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.api.MeasurementResponse;

public class CreateMeasurementResponseProvider {

    public ResponseEntity<MeasurementResponse> doResponse(final Measurement measurement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(MeasurementResponse.of(measurement));
    }
}
