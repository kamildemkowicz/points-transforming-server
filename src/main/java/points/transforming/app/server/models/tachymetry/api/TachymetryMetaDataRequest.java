package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TachymetryMetaDataRequest {
    @NotBlank
    private final String tachymetrName;

    @NotBlank
    private final String tachymetrType;

    @NotNull
    private final Double temperature;

    @NotNull
    private final Long pressure;
}
