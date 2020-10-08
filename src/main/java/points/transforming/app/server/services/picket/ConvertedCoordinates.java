package points.transforming.app.server.services.picket;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConvertedCoordinates {
    private final BigDecimal coordinateX;
    private final BigDecimal coordinateY;
}
