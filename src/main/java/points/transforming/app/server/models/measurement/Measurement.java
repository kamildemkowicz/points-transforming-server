package points.transforming.app.server.models.measurement;

import org.springframework.format.annotation.DateTimeFormat;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.user.User;

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
    private String measurementInternalId;
    @NotNull
    private int version;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "measurement")
    private List<Picket> pickets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeasurementInternalId() {
        return measurementInternalId;
    }

    public void setMeasurementInternalId(String measurementInternalId) {
        this.measurementInternalId = measurementInternalId;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                getVersion() == that.getVersion() &&
                getMeasurementInternalId().equals(that.getMeasurementInternalId()) &&
                getName().equals(that.getName()) &&
                getCreationDate().equals(that.getCreationDate()) &&
                Objects.equals(getEndDate(), that.getEndDate()) &&
                getPlace().equals(that.getPlace()) &&
                getOwner().equals(that.getOwner()) &&
                getUser().equals(that.getUser()) &&
                Objects.equals(getPickets(), that.getPickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMeasurementInternalId(), getVersion(), getName(), getCreationDate(), getEndDate(), getPlace(), getOwner(), getUser(), getPickets());
    }
}
