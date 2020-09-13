package points.transforming.app.server.models.tachymetry.api;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointResponse {
    private final String name;
    private final BigDecimal coordinateX;
    private final BigDecimal coordinateY;
}
