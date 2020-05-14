package points.transforming.app.server.models.measurement;

import points.transforming.app.server.models.picket.PicketWriteModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeasurementWriteModel {
    private String name;
    private String place;
    private List<PicketWriteModel> pickets = new ArrayList<>();

    public MeasurementWriteModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<PicketWriteModel> getPickets() {
        return pickets;
    }

    public void setPickets(List<PicketWriteModel> pickets) {
        this.pickets = pickets;
    }

    public Measurement toMeasurement() {
        var result = new Measurement();
        pickets.forEach(picketWriteModel -> picketWriteModel.setMeasurement(result));
        result.setName(name);
        result.setPlace(place);
        result.setPickets(pickets
                .stream()
                .map(PicketWriteModel::toPicket)
                .collect(Collectors.toList()));

        return result;
    }
}
