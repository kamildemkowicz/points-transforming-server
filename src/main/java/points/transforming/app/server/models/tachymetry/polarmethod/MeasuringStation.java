package points.transforming.app.server.models.tachymetry.polarmethod;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeasuringStation {
    private final Long stationNumber;
    private final String stationName;
    private final Point startingPoint;
    private final Point endPoint;
    private final List<Point> measuringPickets;
}
