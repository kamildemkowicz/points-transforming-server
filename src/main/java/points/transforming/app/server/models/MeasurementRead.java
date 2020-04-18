package points.transforming.app.server.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeasurementRead {
    private final int id;
    private final String name;
    private final String place;
    private final LocalDateTime creationDate;
    private final List<PicketRead> pickets;

    public MeasurementRead(Measurement measurement) {
        this.id = measurement.getId();
        this.name = measurement.getName();
        this.place = measurement.getName();
        this.creationDate = measurement.getCreationDate();
        this.pickets = measurement
                .getPickets()
                .stream()
                .map(PicketRead::new)
                .collect(Collectors.toList());
    }

    public int getId() {
        return id;
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

    public List<PicketRead> getPickets() {
        return pickets;
    }

    private List<Picket> createPicketsList(List<Picket> pickets) {
        return new ArrayList<>(pickets);
    }
}
