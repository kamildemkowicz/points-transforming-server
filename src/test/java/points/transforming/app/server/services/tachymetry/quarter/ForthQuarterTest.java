package points.transforming.app.server.services.tachymetry.quarter;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import points.transforming.app.server.UnitTestWithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class ForthQuarterTest {

    @Test
    public void shouldForthQuarterIsCalculatedAzimuthCorrectly() {
        // given
        final var forthQuarter = new ForthQuarter(BigDecimal.valueOf(60), BigDecimal.valueOf(-24));

        // when
        final var result = forthQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(375.7763));
    }

    @Test
    public void shouldReturn300GradAzimuthWhenDifferenceXIs0AndDifferenceYIsGreaterThan0() {
        // given
        final var forthQuarter = new ForthQuarter(BigDecimal.ZERO, BigDecimal.valueOf(20));

        // when
        final var result = forthQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(300));
    }

    @Test
    public void shouldReturn0GradAzimuthWhenDifferenceXIsLessThan0AndDifferenceYIs0() {
        // given
        final var forthQuarter = new ForthQuarter(BigDecimal.valueOf(-20), BigDecimal.ZERO);

        // when
        final var result = forthQuarter.calculateAzimuth();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(0));
    }
}
