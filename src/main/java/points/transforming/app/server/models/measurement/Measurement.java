package points.transforming.app.server.models.measurement;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Measurement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String measurementInternalId;

    private Integer version;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime endDate;

    private String place;

    private String owner;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

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
