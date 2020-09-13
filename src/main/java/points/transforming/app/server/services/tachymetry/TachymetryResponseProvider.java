package points.transforming.app.server.services.tachymetry;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.tachymetry.api.*;
import points.transforming.app.server.models.tachymetry.polarmethod.MeasuringStation;

public class TachymetryResponseProvider {

    public ResponseEntity<TachymetryResponse> doResponse(final String internalMeasurementId,
                                                         final List<MeasuringStation> measuringStations,
                                                         final TachymetryMetaDataRequest tachymetryMetaData) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(TachymetryResponse.builder()
                .internalMeasurementId(internalMeasurementId)
                .tachymetryMetaData(TachymetryMetaDataResponse.builder()
                    .tachymetrName(tachymetryMetaData.getTachymetrName())
                    .tachymetrType(tachymetryMetaData.getTachymetrType())
                    .pressure(tachymetryMetaData.getPressure())
                    .temperature(tachymetryMetaData.getTemperature())
                    .build()
                )
                .measuringStations(measuringStations.stream()
                    .map(MeasuringStationMappers::of)
                    .collect(Collectors.toUnmodifiableList())
                )
                .build()
            );
    }
}
