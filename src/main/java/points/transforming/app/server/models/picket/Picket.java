package points.transforming.app.server.models.picket;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import points.transforming.app.server.models.measurement.Measurement;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "measurement"})
public class Picket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String picketInternalId;
    @NotNull
    private String name;
    @NotNull
    private double longitude;
    @NotNull
    private double latitude;
    @Column(name = "coordinate_x_2000")
    private double coordinateX2000;
    @Column(name = "coordinate_y_2000")
    private double coordinateY2000;

    @ManyToOne
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;
}
