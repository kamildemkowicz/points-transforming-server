package points.transforming.app.server.models.picket;

import points.transforming.app.server.models.measurement.Measurement;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Picket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String picketInternalId;
    @NotNull
    private String name;
    @NotNull
    private double coordinateX;
    @NotNull
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

    public String getPicketInternalId() {
        return picketInternalId;
    }

    public void setPicketInternalId(String picketInternalId) {
        this.picketInternalId = picketInternalId;
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

    public String getName() {
        return name;
    }

    public void setName(String picketNumber) {
        this.name = picketNumber;
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
                getPicketInternalId().equals(picket.getPicketInternalId()) &&
                getName().equals(picket.getName()) &&
                getMeasurement().equals(picket.getMeasurement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPicketInternalId(), getName(), getCoordinateX(), getCoordinateY(), getMeasurement());
    }
}
