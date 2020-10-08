package points.transforming.app.server.models.picket;

import lombok.Getter;
import lombok.Setter;
import points.transforming.app.server.models.measurement.Measurement;

@Getter
@Setter
public class PicketWriteModel {
    private String name;
    private double coordinateX;
    private double coordinateY;
    private double coordinateX2000;
    private double coordinateY2000;
    private Measurement measurement;
    private String picketInternalId;

    public Picket toPicket() {
        var picket = new Picket();
        picket.setName(name);
        picket.setCoordinateX(coordinateX);
        picket.setCoordinateY(coordinateY);
        picket.setCoordinateX2000(coordinateX2000);
        picket.setCoordinateY2000(coordinateY2000);
        picket.setMeasurement(measurement);
        picket.setPicketInternalId(picketInternalId);

        return picket;
    }
}
