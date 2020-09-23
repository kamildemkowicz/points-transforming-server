package points.transforming.app.server.services.tachymetry;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.services.PicketService;
import points.transforming.app.server.services.measurement.MeasurementService;
import points.transforming.app.server.services.measurement.TestMeasurementServiceFactory;

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
    public void shouldCalculateTachymetryCorrectlyWhenRequestIsProper() {
        // given
        final var measurement = testMeasurementServiceFactory.createValidMeasurement();
        final var tachymetryRequest = testTachymetryServiceFactory.createValidTachymetryRequest();

        when(measurementService.getMeasurement("MES-1")).thenReturn(measurement);

        final var result = tachymetryService.calculateTachymetry(tachymetryRequest);
    }
}
