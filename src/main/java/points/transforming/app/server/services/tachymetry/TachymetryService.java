package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import points.transforming.app.server.exceptions.tachymetry.ControlNetworkPointsException;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.picket.PicketMappers;
import points.transforming.app.server.models.tachymetry.MeasuringStation;
import points.transforming.app.server.models.tachymetry.Tachymetry;
import points.transforming.app.server.models.tachymetry.TachymetryMappers;
import points.transforming.app.server.models.tachymetry.TachymetryPicketMeasured;
import points.transforming.app.server.models.tachymetry.api.MeasuringStationRequest;
import points.transforming.app.server.models.tachymetry.api.PicketMeasurementDataRequest;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.models.tachymetry.polarmethod.Azimuth;
import points.transforming.app.server.models.tachymetry.polarmethod.MeasuringStationDto;
import points.transforming.app.server.models.tachymetry.polarmethod.PointDto;
import points.transforming.app.server.repositories.MeasuringStationRepository;
import points.transforming.app.server.repositories.TachymetryPicketMeasuredRepository;
import points.transforming.app.server.repositories.TachymetryRepository;
import points.transforming.app.server.services.picket.CoordinatesConversionService;
import points.transforming.app.server.services.picket.PicketService;
import points.transforming.app.server.services.measurement.MeasurementService;

@Service
@AllArgsConstructor
public class TachymetryService {

    private final MeasurementService measurementService;
    private final PicketService picketService;
    private final TachymetryValidator tachymetryValidator;
    private final TachymetryRepository tachymetryRepository;
    private final TachymetryPicketMeasuredRepository tachymetryPicketMeasuredRepository;
    private final MeasuringStationRepository measuringStationRepository;
    private final CoordinatesConversionService coordinatesConversionService;

    @Transactional
    public List<MeasuringStationDto> calculateTachymetry(final TachymetryRequest tachymetryRequest) {
        final var calculatedMeasuringStationsDto = new ArrayList<MeasuringStationDto>();
        final var calculatedMeasuringStations = new ArrayList<MeasuringStation>();
        final var measurement = measurementService.getMeasurement(tachymetryRequest.getInternalMeasurementId());
        tachymetryValidator.validateTachymetryRequest(tachymetryRequest);

        final var highestPicketInternalId = picketService.getHighestInternalId();
        final var tachymetry = TachymetryMappers.toTachymetry(measurement, tachymetryRequest.getTachymetryMetaData());

        tachymetryRequest.getMeasuringStations().forEach(measuringStation -> {
            final var station = calculateMeasuringStation(measuringStation, measurement, highestPicketInternalId, tachymetry);
            calculatedMeasuringStationsDto.add(station.getFirst());
            calculatedMeasuringStations.add(station.getSecond());
        });
        tachymetry.setMeasuringStations(calculatedMeasuringStations);
        tachymetryRepository.save(tachymetry);

        return calculatedMeasuringStationsDto;
    }

    private Pair<MeasuringStationDto, MeasuringStation> calculateMeasuringStation(final MeasuringStationRequest measuringStationRequest,
                                                                                  final Measurement measurement, final AtomicInteger highestPicketInternalId,
                                                                                  final Tachymetry tachymetry) {
        final var measuringStation = TachymetryMappers.toMeasuringStation(measuringStationRequest, tachymetry);
        final var startingControlNetworkPoint = PicketMappers.toPicket(measuringStationRequest.getStartingPoint(), measurement,
            "PIC-" + highestPicketInternalId.incrementAndGet());
        final var endControlNetworkPoint = PicketMappers.toPicket(measuringStationRequest.getEndPoint(), measurement,
            "PIC-" + highestPicketInternalId.incrementAndGet());

        final var startingPointGoogleCoordinates = coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(startingControlNetworkPoint,
            measurement.getDistrictId());
        final var endPointGoogleCoordinates = coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(endControlNetworkPoint,
            measurement.getDistrictId());

        startingControlNetworkPoint.setCoordinateX(startingPointGoogleCoordinates.getCoordinateX().doubleValue());
        startingControlNetworkPoint.setCoordinateY(startingPointGoogleCoordinates.getCoordinateY().doubleValue());
        endControlNetworkPoint.setCoordinateX(endPointGoogleCoordinates.getCoordinateX().doubleValue());
        endControlNetworkPoint.setCoordinateY(endPointGoogleCoordinates.getCoordinateY().doubleValue());

        measuringStation.setStartingPointInternalId(startingControlNetworkPoint.getPicketInternalId());
        measuringStation.setEndPointInternalId(endControlNetworkPoint.getPicketInternalId());

        picketService.savePickets(List.of(startingControlNetworkPoint, endControlNetworkPoint));

        final var stationAzimuth = createAzimuthForStation(startingControlNetworkPoint, endControlNetworkPoint);

        final var pointsCalculatedDto = new ArrayList<PointDto>();
        final var measuringPicketsToSave = new ArrayList<Picket>();
        final var tachymetryPickets = new ArrayList<TachymetryPicketMeasured>();

        measuringStationRequest.getPicketsMeasurementData().forEach(picketMeasurement -> {
            final var picketAzimuth = stationAzimuth.getValue().add(picketMeasurement.getAngle());
            tachymetryPickets.add(createTachymetryPicketData(highestPicketInternalId, measuringStation, picketMeasurement));
            final var calculatedPicket = createPicket(picketAzimuth, highestPicketInternalId, picketMeasurement, measurement, measuringStationRequest);
            measuringPicketsToSave.add(calculatedPicket);

            pointsCalculatedDto.add(PointDto.builder()
                .name(picketMeasurement.getPicketName())
                .calculatedPicket(calculatedPicket)
                .angle(picketMeasurement.getAngle())
                .distance(picketMeasurement.getDistance())
                .build());
        });

        picketService.savePickets(measuringPicketsToSave);
        measuringStation.setTachymetryPicketsMeasured(tachymetryPickets);
        measuringStationRepository.save(measuringStation);

        return Pair.of(MeasuringStationDto.builder()
            .stationName(measuringStationRequest.getStationName())
            .stationNumber(measuringStationRequest.getStationNumber())
            .startingPoint(startingControlNetworkPoint)
            .endPoint(endControlNetworkPoint)
            .measuringPickets(pointsCalculatedDto)
            .build(), measuringStation);
    }

    private Picket createPicket(final BigDecimal picketAzimuth, final AtomicInteger highestPicketInternalId,
                                final PicketMeasurementDataRequest picketMeasurement, final Measurement measurement,
                                final MeasuringStationRequest measuringStationRequest) {
        final var measuringPicket = new Picket();
        measuringPicket.setPicketInternalId("PIC-" + highestPicketInternalId.get());
        measuringPicket.setName(picketMeasurement.getPicketName());
        measuringPicket.setCoordinateX2000(calculateCoordinate(
            measuringStationRequest.getStartingPoint().getCoordinateX(),
            calculateDifferenceCoordinateX(picketMeasurement.getDistance(), picketAzimuth)
        ).doubleValue());
        measuringPicket.setCoordinateY2000(calculateCoordinate(
            measuringStationRequest.getStartingPoint().getCoordinateY(),
            calculateDifferenceCoordinateY(picketMeasurement.getDistance(), picketAzimuth)
        ).doubleValue());
        final var convertedCoordinates = coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(measuringPicket, measurement.getDistrictId());
        measuringPicket.setMeasurement(measurement);
        measuringPicket.setCoordinateX(convertedCoordinates.getCoordinateX().doubleValue());
        measuringPicket.setCoordinateY(convertedCoordinates.getCoordinateY().doubleValue());

        return measuringPicket;
    }

    private Azimuth createAzimuthForStation(final Picket startingControlNetworkPoint, final Picket endControlNetworkPoint) {
        return Azimuth.builder()
            .startingControlNetworkPoint(startingControlNetworkPoint)
            .endControlNetworkPoint(endControlNetworkPoint)
            .value(calculateAzimuthFromCoordinates(startingControlNetworkPoint, endControlNetworkPoint))
            .build();
    }

    private BigDecimal calculateAzimuthFromCoordinates(final Picket startingControlNetworkPoint, final Picket endControlNetworkPoint) {
        final var differenceX = BigDecimal.valueOf(endControlNetworkPoint.getCoordinateX2000())
            .subtract(BigDecimal.valueOf(startingControlNetworkPoint.getCoordinateX2000()));
        final var differenceY = BigDecimal.valueOf(endControlNetworkPoint.getCoordinateY2000())
            .subtract(BigDecimal.valueOf(startingControlNetworkPoint.getCoordinateY2000()));

        if (differenceX.equals(BigDecimal.valueOf(0.0)) && differenceY.equals(BigDecimal.valueOf(0.0))) {
            throw new ControlNetworkPointsException(startingControlNetworkPoint.getName(), endControlNetworkPoint.getName());
        }

        return QuarterStrategyBuilder.buildQuarterStrategy(differenceX, differenceY).calculateAzimuth();
    }

    private BigDecimal calculateCoordinate(final BigDecimal controlNetworkPointCoordinate, final BigDecimal offset) {
        return controlNetworkPointCoordinate.add(offset);
    }

    private BigDecimal calculateDifferenceCoordinateX(final BigDecimal distance, final BigDecimal angle) {
        return BigDecimal.valueOf(Math.cos(TachymetryUtils.calculateFromGradToRadian(angle.doubleValue())) * distance.doubleValue())
            .setScale(2, RoundingMode.CEILING);
    }

    private BigDecimal calculateDifferenceCoordinateY(final BigDecimal distance, final BigDecimal angle) {
        return BigDecimal.valueOf(Math.sin(TachymetryUtils.calculateFromGradToRadian(angle.doubleValue())) * distance.doubleValue())
            .setScale(2, RoundingMode.CEILING);
    }

    private TachymetryPicketMeasured createTachymetryPicketData(final AtomicInteger highestPicketInternalId, final MeasuringStation measuringStation,
                                                                final PicketMeasurementDataRequest picketMeasurement) {
        final var tachymetryPicketMeasured = TachymetryMappers.toTachymetryPicketMeasured("PIC-" + highestPicketInternalId.incrementAndGet(),
            picketMeasurement, measuringStation);
        tachymetryPicketMeasuredRepository.save(tachymetryPicketMeasured);

        return tachymetryPicketMeasured;
    }
}
