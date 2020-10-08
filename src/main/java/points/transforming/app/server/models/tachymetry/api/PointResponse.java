package points.transforming.app.server.models.tachymetry.api;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.picket.Picket;

@Getter
@Builder
public class PointResponse {
    private final String name;
    private final BigDecimal angle;
    private final BigDecimal distance;
    private final Picket calculatedPicket;
}
