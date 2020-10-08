package points.transforming.app.server.models.tachymetry.api;

import java.util.stream.Collectors;

import points.transforming.app.server.models.tachymetry.polarmethod.MeasuringStationDto;

public final class MeasuringStationMappers {

    public static MeasuringStationResponse of(final MeasuringStationDto measuringStation) {
        return MeasuringStationResponse.builder()
            .stationName(measuringStation.getStationName())
            .stationNumber(measuringStation.getStationNumber())
            .startingPoint(measuringStation.getStartingPoint())
            .endPoint(measuringStation.getEndPoint())
            .measuringPickets(measuringStation.getMeasuringPickets().stream()
                .map(PointMappers::toPointResponse)
                .collect(Collectors.toUnmodifiableList()))
            .build();
    }
}
