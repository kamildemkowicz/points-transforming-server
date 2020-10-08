package points.transforming.app.server.models.measurement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import points.transforming.app.server.models.picket.PicketWriteModel;
import points.transforming.app.server.models.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementWriteModel {
    private String name;
    private String place;
    private String owner;
    private Integer districtId;
    private List<PicketWriteModel> pickets = new ArrayList<>();

    public Measurement toMeasurement(User user) {
        var result = new Measurement();
        pickets.forEach(picketWriteModel -> picketWriteModel.setMeasurement(result));
        result.setName(name);
        result.setPlace(place);
        result.setOwner(owner);
        result.setUser(user);
        result.setDistrictId(districtId);
        result.setPickets(pickets
                .stream()
                .map(PicketWriteModel::toPicket)
                .collect(Collectors.toList()));

        return result;
    }
}
