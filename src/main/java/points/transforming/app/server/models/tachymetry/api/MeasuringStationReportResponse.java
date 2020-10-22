package points.transforming.app.server.models.tachymetry.api;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeasuringStationReportResponse {
    private final Long stationNumber;
    private final String stationName;
    private final PicketResponse startingPoint;
    private final PicketResponse endPoint;
    private final List<PointReportResponse> measuringPickets;
}
