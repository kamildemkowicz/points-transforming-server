package points.transforming.app.server.services.tachymetry;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.exceptions.tachymetry.EmptyMeasuringStationsListException;

@UnitTestWithMockito
public class TachimetryValidatorTest {

    private final TestTachymetryServiceFactory testTachymetryServiceFactory = new TestTachymetryServiceFactory();

    @InjectMocks
    private TachymetryValidator tachymetryValidator;

    @Test
    public void shouldThrowExceptionWhenTachymetryRequestHasEmptyMeasuringStationsList() {
        // given
        final var tachymetryRequest = testTachymetryServiceFactory.createTachymetryRequestWithoutMeasuringStations();

        // when then
        assertThatThrownBy(() -> tachymetryValidator.validateTachymetryRequest(tachymetryRequest))
            .isInstanceOf(EmptyMeasuringStationsListException.class)
            .hasFieldOrPropertyWithValue("measurementId", "MES-1");
    }

    @Test
    public void shouldNotThrowExceptionWhenTachymetryRequestIsCorrect() {
        // given
        final var tachymetryRequest = testTachymetryServiceFactory.createValidTachymetryRequest();

        // when then
        assertThatCode(() -> tachymetryValidator.validateTachymetryRequest(tachymetryRequest))
            .doesNotThrowAnyException();
    }
}
