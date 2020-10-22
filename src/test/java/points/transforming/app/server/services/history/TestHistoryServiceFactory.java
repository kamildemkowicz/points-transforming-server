package points.transforming.app.server.services.history;

import java.util.Random;

import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;

public class TestHistoryServiceFactory {
    private final Random random;

    public TestHistoryServiceFactory() {
        random = new Random();
    }

    public Picket createValidPicket(final Integer picketNumber, final Measurement measurement) {
        final var picket = new Picket();

        picket.setName("picket" + picketNumber);
        picket.setLongitude(100 * random.nextDouble());
        picket.setLatitude(100 * random.nextDouble());
        picket.setPicketInternalId("PIC-" + picketNumber);
        picket.setMeasurement(measurement);

        return picket;
    }
}
