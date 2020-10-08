package points.transforming.app.server.models.tachymetry;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TachymetryPicketMeasured {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotNull
    @Positive
    private Double distance;

    @NotBlank
    private Double angle;

    @ManyToOne
    @JoinColumn(name = "measuring_station_id")
    private MeasuringStation measuringStation;

    @NotBlank
    private String picketInternalId;
}
