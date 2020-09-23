package points.transforming.app.server.services.measurement;

import java.time.LocalDateTime;
import java.util.List;

import points.transforming.app.server.models.measurement.Measurement;

public class TestMeasurementServiceFactory {

    public Measurement createValidMeasurement() {
        final var measurement = new Measurement();
        measurement.setName("Measurement1");
        measurement.setCreationDate(LocalDateTime.now());
        measurement.setOwner("owner1");
        measurement.setPlace("place1");
        measurement.setMeasurementInternalId("MES-1");
        measurement.setVersion(1);
        measurement.setPickets(List.of());

        return measurement;
    }
}
