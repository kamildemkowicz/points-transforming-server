package points.transforming.app.server.models.measurement;

import points.transforming.app.server.models.picket.PicketReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MeasurementReadModel {
    private int id;
    private String name;
    private String measurementInternalId;
    private String place;
    private String owner;
    private LocalDateTime creationDate;
    private List<PicketReadModel> pickets;

    public MeasurementReadModel() {}

    public MeasurementReadModel(Measurement measurement) {
        this.id = measurement.getId();
        this.name = measurement.getName();
        this.measurementInternalId = measurement.getMeasurementInternalId();
        this.place = measurement.getPlace();
        this.owner = measurement.getOwner();
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

    public String getOwner() {
        return owner;
    }

    public String getMeasurementInternalId() {
        return measurementInternalId;
    }
}
