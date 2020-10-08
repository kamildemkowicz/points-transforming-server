package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class TachymetryMetaDataRequest {
    @NotBlank
    private final String tachymetryName;

    @NotBlank
    private final String tachymetrType;

    @Nullable
    @PositiveOrZero
    private final Double temperature;

    @Nullable
    @PositiveOrZero
    private final Long pressure;
}
