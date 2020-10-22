package points.transforming.app.server.models.tachymetry.api;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointReportResponse {
    private final String name;
    private final BigDecimal angle;
    private final BigDecimal distance;
    private final PicketResponse calculatedPicket;
}
