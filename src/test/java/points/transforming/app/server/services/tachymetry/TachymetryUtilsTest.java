package points.transforming.app.server.services.tachymetry;

import org.junit.jupiter.api.Test;
import points.transforming.app.server.UnitTestWithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class TachymetryUtilsTest {

    @Test
    public void shouldCalculateFromRadianToGradWorksAsExpected() {
        // when
        final var result = TachymetryUtils.calculateFromGradToRadian(100);

        assertThat(result).isEqualTo(1.5707963267948966);
    }

    @Test
    public void shouldCalculateFromGradToRadianWorksAsExpected() {
        // when
        final var result = TachymetryUtils.calculateFromRadianToGrad(2.5);

        assertThat(result).isEqualTo(159.15494309189535);
    }
}
