package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TachymetryMetaDataRequest {
    @NotNull
    private final String tachymetrName;

    @NotNull
    private final String tachymetrType;

    @NotNull
    private final Double temperature;

    @NotNull
    private final Integer pressure;
}
