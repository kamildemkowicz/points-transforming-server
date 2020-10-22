package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TachymetryRequest {

    @NotBlank
    private final String internalMeasurementId;

    @NotNull
    private final TachymetryMetaDataRequest tachymetryMetaData;

    @NotEmpty
    @NotNull
    private final List<MeasuringStationRequest> measuringStations;

}
