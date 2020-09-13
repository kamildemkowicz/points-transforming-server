package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.picket.PicketMappers;
import points.transforming.app.server.models.tachymetry.api.MeasuringStationRequest;
import points.transforming.app.server.models.tachymetry.api.PointMappers;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.models.tachymetry.polarmethod.Azimuth;
import points.transforming.app.server.models.tachymetry.polarmethod.MeasuringStation;
import points.transforming.app.server.models.tachymetry.polarmethod.Point;
import points.transforming.app.server.services.PicketService;
import points.transforming.app.server.services.measurement.MeasurementService;

@Service
@AllArgsConstructor
public class TachymetryService {

    private final MeasurementService measurementService;
    private final PicketService picketService;

    @Transactional
    public List<MeasuringStation> calculateTachymetry(final TachymetryRequest tachymetryRequest) {
        final var calculatedMeasuringStations = new ArrayList<MeasuringStation>();
        final var measurement = measurementService.getMeasurement(tachymetryRequest.getInternalMeasurementId());
        tachymetryRequest.getMeasuringStations().forEach(measuringStation -> calculatedMeasuringStations.add(calculateMeasuringStation(measuringStation,
            measurement)));

        return calculatedMeasuringStations;
    }

    private MeasuringStation calculateMeasuringStation(final MeasuringStationRequest measuringStationRequest, final Measurement measurement) {
        final var startingControlNetworkPoint = PointMappers.toPoint(measuringStationRequest.getStartingPoint(), measurement);
        final var endControlNetworkPoint = PointMappers.toPoint(measuringStationRequest.getEndPoint(), measurement);

        final var controlNetworkPointsToSave = createPicketsFromMeasuringPoints(List.of(startingControlNetworkPoint, endControlNetworkPoint));
        picketService.setInternalIdsForNewPickets(controlNetworkPointsToSave);
        picketService.savePickets(controlNetworkPointsToSave);

        final var stationAzimuth = createAzimuthForStation(startingControlNetworkPoint, endControlNetworkPoint);
        final var measuringPickets = new ArrayList<Point>();

        measuringStationRequest.getPicketsMeasurementData().forEach(picketMeasurement -> {
            final var measuringPicket = new Point();
            final var picketAzimuth = stationAzimuth.getValue().add(picketMeasurement.getAngle());
            measuringPicket.setName(picketMeasurement.getPicketName());
            measuringPicket.setCoordinateX(calculateCoordinate(
                measuringStationRequest.getStartingPoint().getCoordinateX(),
                calculateDifferenceCoordinateX(picketMeasurement.getDistance(), picketAzimuth)
            ));
            measuringPicket.setCoordinateY(calculateCoordinate(
                measuringStationRequest.getStartingPoint().getCoordinateY(),
                calculateDifferenceCoordinateY(picketMeasurement.getDistance(), picketAzimuth)
            ));
            measuringPicket.setMeasurement(measurement);
            measuringPickets.add(measuringPicket);
        });

        final var measuringPicketsToSave = createPicketsFromMeasuringPoints(measuringPickets);
        picketService.setInternalIdsForNewPickets(measuringPicketsToSave);
        picketService.savePickets(measuringPicketsToSave);

        return MeasuringStation.builder()
            .stationName(measuringStationRequest.getStationName())
            .stationNumber(measuringStationRequest.getStationNumber())
            .startingPoint(startingControlNetworkPoint)
            .endPoint(endControlNetworkPoint)
            .measuringPickets(measuringPickets)
            .build();
    }

    private Azimuth createAzimuthForStation(final Point startingControlNetworkPoint, final Point endControlNetworkPoint) {
        return Azimuth.builder()
            .startingControlNetworkPoint(startingControlNetworkPoint)
            .endControlNetworkPoint(endControlNetworkPoint)
            .value(calculateAzimuthFromCoordinates(startingControlNetworkPoint, endControlNetworkPoint))
            .build();
    }

    private BigDecimal calculateAzimuthFromCoordinates(final Point startingControlNetworkPoint, final Point endControlNetworkPoint) {
        final var differenceX = endControlNetworkPoint.getCoordinateX().subtract(startingControlNetworkPoint.getCoordinateX());
        final var differenceY = endControlNetworkPoint.getCoordinateY().subtract(startingControlNetworkPoint.getCoordinateY());

        if (differenceX.equals(BigDecimal.ZERO) && differenceY.equals(BigDecimal.ZERO)) {
            throw new MeasurementNotFoundException("exception");
        }

        return QuarterStrategyBuilder.buildQuarterStrategy(differenceX, differenceY).calculateAzimuth();
    }

    private BigDecimal calculateCoordinate(final BigDecimal controlNetworkPointCoordinate, final BigDecimal offset) {
        return controlNetworkPointCoordinate.add(offset);
    }

    private BigDecimal calculateDifferenceCoordinateX(final BigDecimal distance, final BigDecimal angle) {
        return BigDecimal.valueOf(Math.cos(TachymetryUtils.calculateFromGradToRadian(angle.doubleValue())) * distance.doubleValue());
    }

    private BigDecimal calculateDifferenceCoordinateY(final BigDecimal distance, final BigDecimal angle) {
        return BigDecimal.valueOf(Math.sin(TachymetryUtils.calculateFromGradToRadian(angle.doubleValue())) * distance.doubleValue());
    }

    private List<Picket> createPicketsFromMeasuringPoints(final List<Point> points) {
        return points.stream()
            .map(PicketMappers::toPicket)
            .collect(Collectors.toUnmodifiableList());
    }
}
