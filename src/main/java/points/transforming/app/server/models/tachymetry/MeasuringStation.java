package points.transforming.app.server.models.tachymetry;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MeasuringStation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotNull
    @Positive
    private Long stationNumber;

    @NotBlank
    private String stationName;

    @NotBlank
    private String startingPointInternalId;

    @NotBlank
    private String endPointInternalId;

    @ManyToOne
    @JoinColumn(name = "tachymetry_id")
    private Tachymetry tachymetry;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "measuringStation")
    private List<TachymetryPicketMeasured> tachymetryPicketsMeasured;
}
