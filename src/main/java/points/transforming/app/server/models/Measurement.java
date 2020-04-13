package points.transforming.app.server.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class Measurement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private String place;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "measurements_points",
            joinColumns = @JoinColumn(name = "measurement_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "point_id", referencedColumnName = "id")
    )
    @OrderColumn(name = "order_adding_points")
    private Set<Point> points;

    public void addPoint(Point point) {
        points.add(point);
        point.getMeasurements().add(this);
    }

    public void removePoint(Point point) {
        points.remove(point);
        point.getMeasurements().remove(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    public Set<Point> getPoints() {
        return this.points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Measurement)) return false;
        Measurement that = (Measurement) o;
        return getId() == that.getId() &&
                getName().equals(that.getName()) &&
                getCreationDate().equals(that.getCreationDate()) &&
                Objects.equals(getEndDate(), that.getEndDate()) &&
                getPlace().equals(that.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCreationDate(), getEndDate(), getPlace());
    }
}
