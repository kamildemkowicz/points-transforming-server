package points.transforming.app.server.services.tachymetry.quarter;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import points.transforming.app.server.UnitTestWithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class SecondQuarterTest {

    @Test
    public void shouldSecondQuarterIsCalculatedAzimuthCorrectly() {
        // given
        final var secondQuarter = new SecondQuarter(BigDecimal.valueOf(-64), BigDecimal.valueOf(89));

        // when
        final var result = secondQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(139.6889));
    }

    @Test
    public void shouldReturn100GradAzimuthWhenDifferenceXIs0AndDifferenceYIsGreaterThan0() {
        // given
        final var secondQuarter = new SecondQuarter(BigDecimal.ZERO, BigDecimal.valueOf(20));

        // when
        final var result = secondQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    public void shouldReturn200GradAzimuthWhenDifferenceXIsLessThan0AndDifferenceYIs0() {
        // given
        final var secondQuarter = new SecondQuarter(BigDecimal.valueOf(-20), BigDecimal.ZERO);

        // when
        final var result = secondQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(200));
    }
}
