package points.transforming.app.server.services.measurement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.MeasurementReadModel;

public class MeasurementResponseProvider {

    public ResponseEntity<MeasurementReadModel> doResponse(final Measurement measurement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new MeasurementReadModel(measurement));
    }
}
