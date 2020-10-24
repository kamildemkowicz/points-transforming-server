package points.transforming.app.server.models.picket;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import points.transforming.app.server.models.measurement.Measurement;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "measurement"})
public class Picket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String picketInternalId;
    private String name;
    private Double longitude;
    private Double latitude;
    @Column(name = "coordinate_x_2000")
    private Double coordinateX2000;
    @Column(name = "coordinate_y_2000")
    private Double coordinateY2000;

    @ManyToOne
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;
}
