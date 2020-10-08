package points.transforming.app.server.models.tachymetry.polarmethod;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.picket.Picket;

@Getter
@Builder
public class Azimuth {
    private final Picket startingControlNetworkPoint;
    private final Picket endControlNetworkPoint;
    private final BigDecimal value;
}
