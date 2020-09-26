package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.exceptions.tachymetry.ControlNetworkPointsException;
import points.transforming.app.server.exceptions.tachymetry.EmptyMeasuringStationsListException;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.services.PicketService;
import points.transforming.app.server.services.measurement.MeasurementService;
import points.transforming.app.server.services.measurement.TestMeasurementServiceFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@UnitTestWithMockito
public class TachymetryServiceTest {

    @Mock
    private MeasurementService measurementService;

    @Mock
    private PicketService picketService;

    @Mock
    private TachymetryValidator tachymetryValidator;

    @InjectMocks
    private TachymetryService tachymetryService;

    private final TestTachymetryServiceFactory testTachymetryServiceFactory = new TestTachymetryServiceFactory();
    private final TestMeasurementServiceFactory testMeasurementServiceFactory = new TestMeasurementServiceFactory();

    @Test
    public void shouldThrowMeasurementNotFoundExceptionWhenGetMeasurementThrowsDoesNotExistException() {
        // given
        doThrow(new MeasurementNotFoundException("notExist")).when(measurementService).getMeasurement(anyString());

        final var tachymetryRequestMock = Mockito.mock(TachymetryRequest.class);
        when(tachymetryRequestMock.getInternalMeasurementId()).thenReturn("notExist");

        // when then
        assertThatThrownBy(() -> tachymetryService.calculateTachymetry(tachymetryRequestMock))
            .isInstanceOf(MeasurementNotFoundException.class)
            .hasFieldOrPropertyWithValue("measurementId", "notExist");
    }

    @Test
    public void shouldThrowExceptionWhenTachymetryDoesNotHaveMeasurementStations() {
        // given
        final var measurement = testMeasurementServiceFactory.createValidMeasurement();
        final var tachymetryRequest = testTachymetryServiceFactory.createValidTachymetryRequest();

        when(measurementService.getMeasurement("MES-1")).thenReturn(measurement);
        doThrow(new EmptyMeasuringStationsListException("MES-1")).when(tachymetryValidator).validateTachymetryRequest(tachymetryRequest);

        // when then
        assertThatThrownBy(() -> tachymetryService.calculateTachymetry(tachymetryRequest))
            .isInstanceOf(EmptyMeasuringStationsListException.class)
            .hasFieldOrPropertyWithValue("measurementId", "MES-1");
    }

    @Test
    public void shouldThrowExceptionWhenStartingAndEndPointsAreTheSame() {
        // given
        final var measurement = testMeasurementServiceFactory.createValidMeasurement();
        final var tachymetryRequest = testTachymetryServiceFactory.createInvalidTachymetryRequest();

        when(measurementService.getMeasurement("MES-1")).thenReturn(measurement);

        // when then
        assertThatThrownBy(() -> tachymetryService.calculateTachymetry(tachymetryRequest))
            .isInstanceOf(ControlNetworkPointsException.class)
            .hasFieldOrPropertyWithValue("startingPointName", "name10")
            .hasFieldOrPropertyWithValue("endPointName", "name10");
    }

    @Test
    public void shouldCalculateTachymetryCorrectlyWhenRequestIsProper() {
        // given
        final var measurement = testMeasurementServiceFactory.createValidMeasurement();
        final var tachymetryRequest = testTachymetryServiceFactory.createValidTachymetryRequest();

        when(measurementService.getMeasurement("MES-1")).thenReturn(measurement);

        final var result = tachymetryService.calculateTachymetry(tachymetryRequest);
        assertThat(result.get(0).getStationName()).isEqualTo("Station 1");
        assertThat(result.get(1).getStationName()).isEqualTo("Station 2");
        assertThat(result.get(2).getStationName()).isEqualTo("Station 3");

        assertThat(result.get(0).getStationNumber()).isEqualTo(1L);
        assertThat(result.get(1).getStationNumber()).isEqualTo(2L);
        assertThat(result.get(2).getStationNumber()).isEqualTo(3L);

        assertThat(result.get(0).getStartingPoint().getName()).isEqualTo("name10");
        assertThat(result.get(0).getEndPoint().getName()).isEqualTo("name100");

        assertThat(result.get(1).getStartingPoint().getName()).isEqualTo("name20");
        assertThat(result.get(1).getEndPoint().getName()).isEqualTo("name200");

        assertThat(result.get(2).getStartingPoint().getName()).isEqualTo("name30");
        assertThat(result.get(2).getEndPoint().getName()).isEqualTo("name300");

        assertThat(result.get(0).getMeasuringPickets().size()).isEqualTo(3);
        assertThat(result.get(1).getMeasuringPickets().size()).isEqualTo(3);
        assertThat(result.get(2).getMeasuringPickets().size()).isEqualTo(3);

        assertThat(result.get(0).getMeasuringPickets().get(0).getName()).isEqualTo("picket100");
        assertThat(result.get(0).getMeasuringPickets().get(0).getCoordinateX()).isGreaterThan(BigDecimal.ZERO);
        assertThat(result.get(0).getMeasuringPickets().get(0).getCoordinateY()).isGreaterThan(BigDecimal.ZERO);
        assertThat(result.get(0).getMeasuringPickets().get(0).getMeasurement()).isEqualTo(measurement);
    }
}
