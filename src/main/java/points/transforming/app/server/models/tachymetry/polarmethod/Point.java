package points.transforming.app.server.models.tachymetry.polarmethod;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import points.transforming.app.server.models.measurement.Measurement;

@Getter
@Setter
public class Point {
    private String name;
    private BigDecimal coordinateX;
    private BigDecimal coordinateY;
    private Measurement measurement;
}
