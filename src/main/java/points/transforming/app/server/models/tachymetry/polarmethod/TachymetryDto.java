package points.transforming.app.server.models.tachymetry.polarmethod;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.tachymetry.api.TachymetryMetaDataResponse;

@Getter
@Builder
public class TachymetryDto {

    private final String internalMeasurementId;
    private final TachymetryMetaDataResponse tachymetryMetaData;
    private final List<MeasuringStationReportDto> measuringStations;
}
