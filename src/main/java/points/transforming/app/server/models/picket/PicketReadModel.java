package points.transforming.app.server.models.picket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PicketReadModel {
    private int id;
    private String picketInternalId;
    private String name;
    private double coordinateX;
    private double coordinateY;
    private double coordinateX2000;
    private double coordinateY2000;

    public PicketReadModel(Picket picket) {
        this.id = picket.getId();
        this.name = picket.getName();
        this.coordinateX = picket.getCoordinateX();
        this.coordinateY = picket.getCoordinateY();
        this.coordinateX2000 = picket.getCoordinateX2000();
        this.coordinateY2000 = picket.getCoordinateY2000();
        this.picketInternalId = picket.getPicketInternalId();
    }
}
