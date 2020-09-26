package points.transforming.app.server.services.tachymetry.quarter;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import points.transforming.app.server.UnitTestWithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class FirstQuarterTest {

    @Test
    public void shouldFirstQuarterIsCalculatedAzimuthCorrectly() {
        // given
        final var firstQuarter = new FirstQuarter(BigDecimal.TEN, BigDecimal.valueOf(20));

        // when
        final var result = firstQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(70.4833));
    }

    @Test
    public void shouldReturn100GradAzimuthWhenDifferenceXIs0AndDifferenceYIsGreaterThan0() {
        // given
        final var firstQuarter = new FirstQuarter(BigDecimal.ZERO, BigDecimal.valueOf(20));

        // when
        final var result = firstQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    public void shouldReturn0GradAzimuthWhenDifferenceXIsGreaterThan0AndDifferenceYIs0() {
        // given
        final var firstQuarter = new FirstQuarter(BigDecimal.TEN, BigDecimal.ZERO);

        // when
        final var result = firstQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }
}
