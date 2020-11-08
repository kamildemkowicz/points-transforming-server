package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.exceptions.tachymetry.ControlNetworkPointsException;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.repositories.MeasuringStationRepository;
import points.transforming.app.server.repositories.TachymetryPicketMeasuredRepository;
import points.transforming.app.server.repositories.TachymetryRepository;
import points.transforming.app.server.services.picket.ConvertedCoordinates;
import points.transforming.app.server.services.picket.CoordinatesConversionService;
import points.transforming.app.server.services.picket.PicketService;
import points.transforming.app.server.services.measurement.MeasurementService;
import points.transforming.app.server.services.measurement.TestMeasurementServiceFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@UnitTestWithMockito
public class TachymetryServiceTest {

    @Mock
    private MeasurementService measurementService;
    @Mock
    private PicketService picketService;
    @Mock
    private TachymetryRepository tachymetryRepository;
    @Mock
    private TachymetryPicketMeasuredRepository tachymetryPicketMeasuredRepository;
    @Mock
    private MeasuringStationRepository measuringStationRepository;
    @Mock
    private CoordinatesConversionService coordinatesConversionService;

    @InjectMocks
    private TachymetryService tachymetryService;

    private final TestTachymetryServiceFactory testTachymetryServiceFactory = new TestTachymetryServiceFactory();
    private final TestMeasurementServiceFactory testMeasurementServiceFactory = new TestMeasurementServiceFactory();

    @Test
    public void shouldThrowMeasurementNotFoundExceptionWhenGetMeasurementThrowsDoesNotExistException() {
        // given
        doThrow(new MeasurementNotFoundException(Error.MEASUREMENT_DOES_NOT_EXIST_PTS100)).when(measurementService).getMeasurement(anyString());

        final var tachymetryRequestMock = Mockito.mock(TachymetryRequest.class);
        final var principalMock = Mockito.mock(Principal.class);
        when(tachymetryRequestMock.getInternalMeasurementId()).thenReturn("notExist");

        // when then
        assertThatThrownBy(() -> tachymetryService.calculateTachymetry(tachymetryRequestMock, principalMock))
            .isInstanceOf(MeasurementNotFoundException.class)
            .hasFieldOrPropertyWithValue("measurementId", "notExist");
    }

    @Test
    public void shouldThrowExceptionWhenStartingAndEndPointsAreTheSame() {
        // given
        final var measurement = testMeasurementServiceFactory.createValidMeasurement();
        final var tachymetryRequest = testTachymetryServiceFactory.createInvalidTachymetryRequest();
        final var coordinateConverterMock = Mockito.mock(ConvertedCoordinates.class);
        final var principalMock = Mockito.mock(Principal.class);
        when(coordinateConverterMock.getCoordinateX()).thenReturn(BigDecimal.valueOf(49.93956789022778));
        when(coordinateConverterMock.getCoordinateY()).thenReturn(BigDecimal.valueOf(19.012730260613353));

        when(measurementService.getMeasurement("MES-1")).thenReturn(measurement);
        when(picketService.getHighestInternalId()).thenReturn(new AtomicInteger(1));
        when(coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(any(Picket.class), anyInt())).thenReturn(coordinateConverterMock);

        // when then
        assertThatThrownBy(() -> tachymetryService.calculateTachymetry(tachymetryRequest, principalMock))
            .isInstanceOf(ControlNetworkPointsException.class)
            .hasFieldOrPropertyWithValue("startingPointName", "name10")
            .hasFieldOrPropertyWithValue("endPointName", "name10");
    }

    @Test
    public void shouldCalculateTachymetryCorrectlyWhenRequestIsProper() {
        // given
        final var measurement = testMeasurementServiceFactory.createValidMeasurement();
        final var tachymetryRequest = testTachymetryServiceFactory.createValidTachymetryRequest();
        final var coordinateConverterMock = Mockito.mock(ConvertedCoordinates.class);
        final var principalMock = Mockito.mock(Principal.class);

        when(coordinateConverterMock.getCoordinateX()).thenReturn(BigDecimal.valueOf(49.93956789022778));
        when(coordinateConverterMock.getCoordinateY()).thenReturn(BigDecimal.valueOf(19.012730260613353));

        when(measurementService.getMeasurement("MES-1")).thenReturn(measurement);
        when(picketService.getHighestInternalId()).thenReturn(new AtomicInteger(1));
        when(coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(any(Picket.class), anyInt())).thenReturn(coordinateConverterMock);

        // when
        final var result = tachymetryService.calculateTachymetry(tachymetryRequest, principalMock);

        // then
        assertThat(result.get(0).getStationName()).isEqualTo("Station 1");
        assertThat(result.get(1).getStationName()).isEqualTo("Station 2");
        assertThat(result.get(2).getStationName()).isEqualTo("Station 3");

        assertThat(result.get(0).getStationNumber()).isEqualTo(1L);
        assertThat(result.get(1).getStationNumber()).isEqualTo(2L);
        assertThat(result.get(2).getStationNumber()).isEqualTo(3L);

        assertThat(result.get(0).getStartingPoint().getPicketInternalId()).isEqualTo("PIC-2");
        assertThat(result.get(0).getEndPoint().getPicketInternalId()).isEqualTo("PIC-3");

        assertThat(result.get(1).getStartingPoint().getPicketInternalId()).isEqualTo("PIC-7");
        assertThat(result.get(1).getEndPoint().getPicketInternalId()).isEqualTo("PIC-8");

        assertThat(result.get(2).getStartingPoint().getPicketInternalId()).isEqualTo("PIC-12");
        assertThat(result.get(2).getEndPoint().getPicketInternalId()).isEqualTo("PIC-13");

        assertThat(result.get(0).getMeasuringPickets().size()).isEqualTo(3);
        assertThat(result.get(1).getMeasuringPickets().size()).isEqualTo(3);
        assertThat(result.get(2).getMeasuringPickets().size()).isEqualTo(3);

        assertThat(result.get(0).getMeasuringPickets().get(0).getCalculatedPicket().getPicketInternalId()).isEqualTo("PIC-4");
        assertThat(result.get(0).getMeasuringPickets().get(0).getAngle()).isGreaterThan(BigDecimal.ZERO);
        assertThat(result.get(0).getMeasuringPickets().get(0).getDistance()).isGreaterThan(BigDecimal.ZERO);

        verify(tachymetryRepository).save(any());
        verify(measuringStationRepository, times(3)).save(any());
        verify(tachymetryPicketMeasuredRepository, times(9)).save(any());
        verify(coordinatesConversionService, times(15)).convertCoordinateFromGeocentricToWgs84(any(Picket.class), anyInt());
    }

    enum Error {
        MEASUREMENT_DOES_NOT_EXIST_PTS100, EMPTY_STATIONS_LIST_PTS301
    }
}
