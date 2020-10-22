package points.transforming.app.server.services.tachymetry;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.tachymetry.api.*;
import points.transforming.app.server.models.tachymetry.polarmethod.MeasuringStationReportDto;
import points.transforming.app.server.models.tachymetry.polarmethod.TachymetryDto;

public class TachymetryResponseProvider {

    public ResponseEntity<TachymetryReportResponse> doResponse(final String internalMeasurementId,
                                                               final List<MeasuringStationReportDto> measuringStations,
                                                               final TachymetryMetaDataRequest tachymetryMetaData) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(TachymetryReportResponse.builder()
                .internalMeasurementId(internalMeasurementId)
                .tachymetryMetaData(TachymetryMetaDataResponse.builder()
                    .tachymetryName(tachymetryMetaData.getTachymetryName())
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

    public ResponseEntity<List<TachymetryReportResponse>> doResponse(final List<TachymetryDto> tachymetries) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tachymetries.stream()
                .map(TachymetryReportResponse::of)
                .collect(Collectors.toUnmodifiableList())
            );
    }
}
