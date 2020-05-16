package points.transforming.app.server.models.picket;

import points.transforming.app.server.models.measurement.Measurement;

public class PicketWriteModel {
    private String name;
    private double coordinateX;
    private double coordinateY;
    private Measurement measurement;
    private String picketInternalId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public String getPicketInternalId() {
        return this.picketInternalId;
    }

    public void setPicketInternalId(String picketInternalId) {
        this.picketInternalId = picketInternalId;
    }

    public Picket toPicket() {
        var picket = new Picket();
        picket.setName(name);
        picket.setCoordinateX(coordinateX);
        picket.setCoordinateY(coordinateY);
        picket.setMeasurement(measurement);
        picket.setPicketInternalId(picketInternalId);

        return picket;
    }
}
