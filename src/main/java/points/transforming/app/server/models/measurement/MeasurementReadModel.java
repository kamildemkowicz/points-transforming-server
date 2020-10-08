package points.transforming.app.server.models.measurement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import points.transforming.app.server.models.picket.PicketReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MeasurementReadModel {
    private int id;
    private String name;
    private String measurementInternalId;
    private String place;
    private String owner;
    private LocalDateTime creationDate;
    private Integer districtId;
    private List<PicketReadModel> pickets;

    public MeasurementReadModel(Measurement measurement) {
        this.id = measurement.getId();
        this.name = measurement.getName();
        this.measurementInternalId = measurement.getMeasurementInternalId();
        this.place = measurement.getPlace();
        this.owner = measurement.getOwner();
        this.creationDate = measurement.getCreationDate();
        this.districtId = measurement.getDistrictId();
        this.pickets = measurement.getPickets()
            .stream()
            .map(PicketReadModel::new)
            .collect(Collectors.toList());
    }
}
