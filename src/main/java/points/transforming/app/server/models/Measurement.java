package points.transforming.app.server.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Measurement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private String place;

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
