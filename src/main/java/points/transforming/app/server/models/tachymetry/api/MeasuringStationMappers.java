package points.transforming.app.server.models.tachymetry.api;

import java.util.stream.Collectors;

import points.transforming.app.server.models.tachymetry.polarmethod.MeasuringStationReportDto;

public final class MeasuringStationMappers {

    public static MeasuringStationReportResponse of(final MeasuringStationReportDto measuringStation) {
        return MeasuringStationReportResponse.builder()
            .stationName(measuringStation.getStationName())
            .stationNumber(measuringStation.getStationNumber())
            .startingPoint(PicketResponse.of(measuringStation.getStartingPoint()))
            .endPoint(PicketResponse.of(measuringStation.getEndPoint()))
            .measuringPickets(measuringStation.getMeasuringPickets().stream()
                .map(PointMappers::toPointResponse)
                .collect(Collectors.toUnmodifiableList()))
            .build();
    }
}
