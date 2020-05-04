package points.transforming.app.server.models.picket;

public class PicketReadModel {
    private int id;
    private String picketId;
    private double coordinateX;
    private double coordinateY;

    public PicketReadModel() {}

    public PicketReadModel(Picket picket) {
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
