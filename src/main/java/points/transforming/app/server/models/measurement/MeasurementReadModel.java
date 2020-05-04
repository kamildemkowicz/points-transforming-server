package points.transforming.app.server.models.measurement;

import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.picket.PicketReadModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeasurementReadModel {
    private int id;
    private String name;
    private String place;
    private LocalDateTime creationDate;
    private List<PicketReadModel> pickets;

    public MeasurementReadModel() {}

    public MeasurementReadModel(Measurement measurement) {
        this.id = measurement.getId();
        this.name = measurement.getName();
        this.place = measurement.getPlace();
        this.creationDate = measurement.getCreationDate();
        this.pickets = measurement
                .getPickets()
                .stream()
                .map(PicketReadModel::new)
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

    public List<PicketReadModel> getPickets() {
        return pickets;
    }

    private List<Picket> createPicketsList(List<Picket> pickets) {
        return new ArrayList<>(pickets);
    }
}
