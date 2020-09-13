package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TachymetryRequest {

    @NotBlank
    private final String internalMeasurementId;

    @NotNull
    private final TachymetryMetaDataRequest tachymetryMetaData;

    @NotNull
    private final List<MeasuringStationRequest> measuringStations;

}
