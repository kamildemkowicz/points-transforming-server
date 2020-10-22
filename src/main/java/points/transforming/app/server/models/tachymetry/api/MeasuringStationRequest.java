package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeasuringStationRequest {

    @NotNull
    @Positive
    private final Long stationNumber;

    @NotBlank
    private final String stationName;

    @NotNull
    private final GeodeticControlNetworkPointRequest startingPoint;

    @NotNull
    private final GeodeticControlNetworkPointRequest endPoint;

    @Builder.Default
    private final List<PicketMeasurementDataRequest> picketsMeasurementData = new ArrayList<>();
}
