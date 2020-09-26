package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GeodeticControlNetworkPointRequest {
    @NotBlank
    private final String name;

    @NotNull
    @Positive
    private final BigDecimal coordinateX;

    @NotNull
    @Positive
    private final BigDecimal coordinateY;
}
