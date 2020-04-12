package points.transforming.app.server.models;

import java.time.LocalDateTime;

public class MeasurementRead {
    private final String name;
    private final String place;
    private final LocalDateTime creationDate;

    public MeasurementRead(Measurement measurement) {
        this.name = measurement.getName();
        this.place = measurement.getName();
        this.creationDate = measurement.getCreationDate();
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
