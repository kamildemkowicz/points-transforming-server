package points.transforming.app.server.models.tachymetry.api;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TachymetryResponse {

    private final String internalMeasurementId;
    private final TachymetryMetaDataResponse tachymetryMetaData;
    private final List<MeasuringStationResponse> measuringStations;

}
