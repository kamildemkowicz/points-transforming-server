package points.transforming.app.server.models.measurement;

import org.springframework.format.annotation.DateTimeFormat;
import points.transforming.app.server.models.picket.Picket;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Measurement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @NotNull
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime creationDate;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime endDate;
    @NotNull
    private String place;
    @NotNull
    private String owner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "measurement")
    private List<Picket> pickets;

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

    public List<Picket> getPickets() {
        return this.pickets;
    }

    public void setPickets(List<Picket> pickets) {
        this.pickets = pickets;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @PrePersist
    private void setCreationDate() {
        this.setCreationDate(LocalDateTime.now());
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
                getPlace().equals(that.getPlace()) &&
                getOwner().equals(that.getOwner()) &&
                Objects.equals(getPickets(), that.getPickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCreationDate(), getEndDate(), getPlace(), getOwner(), getPickets());
    }
}
