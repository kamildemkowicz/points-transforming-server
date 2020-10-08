package points.transforming.app.server.models.tachymetry;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
public class Tachymetry {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String tachymetrType;

    @Nullable
    @PositiveOrZero
    private Double temperature;

    @Nullable
    @Positive
    private Long pressure;

    @NotBlank
    private String measurementInternalId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tachymetry")
    private List<MeasuringStation> measuringStations;
}
