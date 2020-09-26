package points.transforming.app.server.services.tachymetry;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import points.transforming.app.server.UnitTestWithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class TachymetryResponseProviderTest {

    private final TestTachymetryServiceFactory testTachymetryServiceFactory = new TestTachymetryServiceFactory();

    @Test
    public void shouldTachymetryResponseProviderWorksCorrectly() {
        // when
        final var result = new TachymetryResponseProvider().doResponse("MES-1", List.of(),
            testTachymetryServiceFactory.createValidTachymetryMetaDataRequest());

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getInternalMeasurementId()).isEqualTo("MES-1");
    }
}
