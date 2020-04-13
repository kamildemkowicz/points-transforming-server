package points.transforming.app.server.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int picket;
    private double coordinateX;
    private double coordinateY;

    @ManyToMany(mappedBy = "points", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Measurement> measurements;

    public Set<Measurement> getMeasurements() {
        return this.measurements;
    }

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

    public void setMeasurements(Set<Measurement> measurements) {
        this.measurements = measurements;
    }
}
