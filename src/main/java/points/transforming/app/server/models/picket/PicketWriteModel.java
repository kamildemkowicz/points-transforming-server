package points.transforming.app.server.models.picket;

public class PicketWriteModel {
    private String picketId;
    private double coordinateX;
    private double coordinateY;

    public String getPicketId() {
        return picketId;
    }

    public void setPicketId(String picketId) {
        this.picketId = picketId;
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

    public Picket toPicket() {
        var picket = new Picket();
        picket.setPicketId(picketId);
        picket.setCoordinateX(coordinateX);
        picket.setCoordinateY(coordinateY);

        return picket;
    }
}
