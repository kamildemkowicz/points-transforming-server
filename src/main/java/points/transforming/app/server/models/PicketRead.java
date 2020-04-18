package points.transforming.app.server.models;

public class PicketRead {
    private final int id;

    private final String picketId;
    private final double coordinateX;
    private final double coordinateY;

    public PicketRead(Picket picket) {
        this.id = picket.getId();
        this.picketId = picket.getPicketId();
        this.coordinateX = picket.getCoordinateX();
        this.coordinateY = picket.getCoordinateY();
    }

    public int getId() {
        return id;
    }

    public String getPicketId() {
        return picketId;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }
}
