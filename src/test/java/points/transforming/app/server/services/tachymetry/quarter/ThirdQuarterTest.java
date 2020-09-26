package points.transforming.app.server.services.tachymetry.quarter;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import points.transforming.app.server.UnitTestWithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class ThirdQuarterTest {

    @Test
    public void shouldThirdQuarterIsCalculatedAzimuthCorrectly() {
        // given
        final var thirdQuarter = new ThirdQuarter(BigDecimal.valueOf(-34), BigDecimal.valueOf(-24));

        // when
        final var result = thirdQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(239.1307));
    }

    @Test
    public void shouldReturn300GradAzimuthWhenDifferenceXIs0AndDifferenceYIsLessThan0() {
        // given
        final var thirdQuarter = new ThirdQuarter(BigDecimal.ZERO, BigDecimal.valueOf(-20));

        // when
        final var result = thirdQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(300));
    }

    @Test
    public void shouldReturn200GradAzimuthWhenDifferenceXIsLessThan0AndDifferenceYIs0() {
        // given
        final var thirdQuarter = new ThirdQuarter(BigDecimal.valueOf(-20), BigDecimal.ZERO);

        // when
        final var result = thirdQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(200));
    }
}
