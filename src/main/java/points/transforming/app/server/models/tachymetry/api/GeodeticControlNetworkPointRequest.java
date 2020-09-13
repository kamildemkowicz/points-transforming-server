package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GeodeticControlNetworkPointRequest {
    @NotNull
    private final String name;

    @NotNull
    private final BigDecimal coordinateX;

    @NotNull
    private final BigDecimal coordinateY;
}
