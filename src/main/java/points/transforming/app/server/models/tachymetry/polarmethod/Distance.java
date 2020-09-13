package points.transforming.app.server.models.tachymetry.polarmethod;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Distance {
    private final Point startingPoint;
    private final Point endPoint;
    private final BigDecimal value;
}
