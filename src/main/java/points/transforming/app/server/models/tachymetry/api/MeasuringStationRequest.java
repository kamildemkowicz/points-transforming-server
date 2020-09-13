package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeasuringStationRequest {

    @NotNull
    @Positive
    private final Long stationNumber;

    @NotNull
    private final String stationName;

    @NotNull
    private final GeodeticControlNetworkPointRequest startingPoint;

    @NotNull
    private final GeodeticControlNetworkPointRequest endPoint;

    @NotNull
    private final List<PicketMeasurementDataRequest> picketsMeasurementData;
}
