package points.transforming.app.server.models.picket;

public class PicketReadModel {
    private int id;
    private String picketInternalId;
    private String name;
    private double coordinateX;
    private double coordinateY;

    public PicketReadModel() {}

    public PicketReadModel(Picket picket) {
        this.id = picket.getId();
        this.name = picket.getName();
        this.coordinateX = picket.getCoordinateX();
        this.coordinateY = picket.getCoordinateY();
        this.picketInternalId = picket.getPicketInternalId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public String getPicketInternalId() {
        return this.picketInternalId;
    }
}
