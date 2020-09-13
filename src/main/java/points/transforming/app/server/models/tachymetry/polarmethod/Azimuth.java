package points.transforming.app.server.models.tachymetry.polarmethod;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Azimuth {
    private final Point startingControlNetworkPoint;
    private final Point endControlNetworkPoint;
    private final BigDecimal value;
}
