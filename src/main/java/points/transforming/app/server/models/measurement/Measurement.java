package points.transforming.app.server.models.measurement;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
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

    @PrePersist
    private void setCreationDate() {
        this.setCreationDate(LocalDateTime.now());
    }

}
