package points.transforming.app.server.models.tachymetry.api;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.tachymetry.polarmethod.Point;

@Getter
@Builder
public class MeasuringStationResponse {
    private final Long stationNumber;
    private final String stationName;
    private final Point startingPoint;
    private final Point endPoint;
    private final List<PointResponse> measuringPickets;
}
