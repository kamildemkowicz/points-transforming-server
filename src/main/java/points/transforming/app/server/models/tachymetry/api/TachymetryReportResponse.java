package points.transforming.app.server.models.tachymetry.api;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.tachymetry.polarmethod.TachymetryDto;

@Getter
@Builder
public class TachymetryReportResponse {

    private final String internalMeasurementId;
    private final TachymetryMetaDataResponse tachymetryMetaData;
    private final List<MeasuringStationReportResponse> measuringStations;

    public static TachymetryReportResponse of(final TachymetryDto tachymetryDto) {
        return TachymetryReportResponse.builder()
            .internalMeasurementId(tachymetryDto.getInternalMeasurementId())
            .tachymetryMetaData(TachymetryMetaDataResponse.builder()
                .tachymetryName(tachymetryDto.getTachymetryMetaData().getTachymetryName())
                .tachymetrType(tachymetryDto.getTachymetryMetaData().getTachymetrType())
                .pressure(tachymetryDto.getTachymetryMetaData().getPressure())
                .temperature(tachymetryDto.getTachymetryMetaData().getTemperature())
                .build()
            )
            .measuringStations(tachymetryDto.getMeasuringStations().stream()
                .map(MeasuringStationMappers::of)
                .collect(Collectors.toUnmodifiableList())
            )
            .build();
    }
}
