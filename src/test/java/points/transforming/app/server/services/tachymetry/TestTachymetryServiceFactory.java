package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import points.transforming.app.server.models.tachymetry.api.*;

public class TestTachymetryServiceFactory {
    private final Random random;

    public TestTachymetryServiceFactory() {
        random = new Random();
    }

    public TachymetryRequest createValidTachymetryRequest() {
        return TachymetryRequest.builder()
            .internalMeasurementId("MES-1")
            .measuringStations(
                List.of(
                    createValidMeasuringStationRequest(1L),
                    createValidMeasuringStationRequest(2L),
                    createValidMeasuringStationRequest(3L)
                )
            )
            .tachymetryMetaData(createValidTachymetryMetaDataRequest())
            .build();
    }

    private MeasuringStationRequest createValidMeasuringStationRequest(final Long stationNumber) {
        return MeasuringStationRequest.builder()
            .startingPoint(createValidGeodeticControlNetworkPointRequest(stationNumber * 10))
            .endPoint(createValidGeodeticControlNetworkPointRequest(stationNumber * 100))
            .stationName("Station " + stationNumber)
            .stationNumber(stationNumber)
            .picketsMeasurementData(
                List.of(
                    createValidPicketMeasurementDataRequest(),
                    createValidPicketMeasurementDataRequest(),
                    createValidPicketMeasurementDataRequest()
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

    private PicketMeasurementDataRequest createValidPicketMeasurementDataRequest() {
        return PicketMeasurementDataRequest.builder()
            .angle(BigDecimal.ONE)
            .distance(BigDecimal.TEN)
            .picketName("picket")
            .build();
    }

    private TachymetryMetaDataRequest createValidTachymetryMetaDataRequest() {
        return new TachymetryMetaDataRequest("tachimetr1", "LEICA", 25.0, 1001);
    }
}
