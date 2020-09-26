package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import points.transforming.app.server.models.tachymetry.api.*;

public class TestTachymetryServiceFactory {

    public TachymetryRequest createTachymetryRequestWithoutMeasuringStations() {
        return TachymetryRequest.builder()
            .internalMeasurementId("MES-1")
            .measuringStations(List.of())
            .tachymetryMetaData(createValidTachymetryMetaDataRequest())
            .build();
    }

    public TachymetryRequest createInvalidTachymetryRequest() {
        return TachymetryRequest.builder()
            .internalMeasurementId("MES-1")
            .measuringStations(
                List.of(
                    createInvalidMeasuringStationRequest(1L, 100L)
                )
            )
            .tachymetryMetaData(createValidTachymetryMetaDataRequest())
            .build();
    }

    public TachymetryRequest createValidTachymetryRequest() {
        return TachymetryRequest.builder()
            .internalMeasurementId("MES-1")
            .measuringStations(
                List.of(
                    createValidMeasuringStationRequest(1L, 100L),
                    createValidMeasuringStationRequest(2L, 200L),
                    createValidMeasuringStationRequest(3L, 300L)
                )
            )
            .tachymetryMetaData(createValidTachymetryMetaDataRequest())
            .build();
    }

    public TachymetryMetaDataRequest createValidTachymetryMetaDataRequest() {
        return new TachymetryMetaDataRequest("tachimetr1", "LEICA", 25.0, 1001L);
    }

    private MeasuringStationRequest createInvalidMeasuringStationRequest(final Long stationNumber, Long firstPicketNumber) {
        return MeasuringStationRequest.builder()
            .startingPoint(createValidGeodeticControlNetworkPointRequest(stationNumber * 10))
            .endPoint(createValidGeodeticControlNetworkPointRequest(stationNumber * 10))
            .stationName("Station " + stationNumber)
            .stationNumber(stationNumber)
            .picketsMeasurementData(
                List.of(
                    createValidPicketMeasurementDataRequest(firstPicketNumber),
                    createValidPicketMeasurementDataRequest(++firstPicketNumber),
                    createValidPicketMeasurementDataRequest(++firstPicketNumber)
                )
            )
            .build();
    }

    private MeasuringStationRequest createValidMeasuringStationRequest(final Long stationNumber, Long firstPicketNumber) {
        return MeasuringStationRequest.builder()
            .startingPoint(createValidGeodeticControlNetworkPointRequest(stationNumber * 10))
            .endPoint(createValidGeodeticControlNetworkPointRequest(stationNumber * 100))
            .stationName("Station " + stationNumber)
            .stationNumber(stationNumber)
            .picketsMeasurementData(
                List.of(
                    createValidPicketMeasurementDataRequest(firstPicketNumber),
                    createValidPicketMeasurementDataRequest(++firstPicketNumber),
                    createValidPicketMeasurementDataRequest(++firstPicketNumber)
                )
            )
            .build();
    }

    private GeodeticControlNetworkPointRequest createValidGeodeticControlNetworkPointRequest(final Long number) {
        final var coordinate = BigDecimal.valueOf(100.00).add(BigDecimal.valueOf(number));
        return GeodeticControlNetworkPointRequest.builder()
            .name("name" + number)
            .coordinateX(coordinate)
            .coordinateY(coordinate)
            .build();
    }

    private PicketMeasurementDataRequest createValidPicketMeasurementDataRequest(Long firstPicketNumber) {
        return PicketMeasurementDataRequest.builder()
            .angle(BigDecimal.valueOf(RandomUtils.nextDouble() * 400).setScale(4, RoundingMode.CEILING))
            .distance(BigDecimal.valueOf(RandomUtils.nextDouble() * 100).setScale(2, RoundingMode.CEILING))
            .picketName("picket" + firstPicketNumber)
            .build();
    }
}
