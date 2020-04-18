package points.transforming.app.server.models.picket;

import points.transforming.app.server.models.measurement.Measurement;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Picket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String picketId;
    private double coordinateX;
    private double coordinateY;

    @ManyToOne
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPicketId() {
        return picketId;
    }

    public void setPicketId(String picketNumber) {
        this.picketId = picketNumber;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picket)) return false;
        Picket picket = (Picket) o;
        return getId() == picket.getId() &&
                Double.compare(picket.getCoordinateX(), getCoordinateX()) == 0 &&
                Double.compare(picket.getCoordinateY(), getCoordinateY()) == 0 &&
                Objects.equals(getPicketId(), picket.getPicketId()) &&
                getMeasurement().equals(picket.getMeasurement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPicketId(), getCoordinateX(), getCoordinateY(), getMeasurement());
    }
}
