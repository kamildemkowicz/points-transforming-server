package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.services.tachymetry.quarter.FirstQuarter;
import points.transforming.app.server.services.tachymetry.quarter.ForthQuarter;
import points.transforming.app.server.services.tachymetry.quarter.SecondQuarter;
import points.transforming.app.server.services.tachymetry.quarter.ThirdQuarter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@UnitTestWithMockito
public class QuarterStrategyBuilderTest {

    @Test
    public void shouldSelectFirstQuarterWhenDifferenceXAndDifferenceYAreGreaterThan0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.TEN, BigDecimal.TEN);

        // then
        assertThat(result).isExactlyInstanceOf(FirstQuarter.class);
    }

    @Test
    public void shouldSelectSecondQuarterWhenDifferenceXIsLessThan0AndDifferenceYIsGreaterThan0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.valueOf(-10), BigDecimal.TEN);

        // then
        assertThat(result).isExactlyInstanceOf(SecondQuarter.class);
    }

    @Test
    public void shouldSelectThirdQuarterWhenDifferenceXAndDifferenceYAreLessThan0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.valueOf(-10), BigDecimal.valueOf(-10));

        // then
        assertThat(result).isExactlyInstanceOf(ThirdQuarter.class);
    }

    @Test
    public void shouldSelectFirstQuarterWhenDifferenceXIs0AndDifferenceYIs0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.ZERO, BigDecimal.ZERO);

        // then
        assertThat(result).isExactlyInstanceOf(FirstQuarter.class);
    }

    @Test
    public void shouldSelectForthQuarterWhenDifferenceXIsGreaterThan0AndDifferenceYIs0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.TEN, BigDecimal.ZERO);

        // then
        assertThat(result).isExactlyInstanceOf(FirstQuarter.class);
    }

    @Test
    public void shouldSelectForthQuarterWhenDifferenceXIsLessThan0AndDifferenceYIs0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.valueOf(-10), BigDecimal.ZERO);

        // then
        assertThat(result).isExactlyInstanceOf(SecondQuarter.class);
    }

    @Test
    public void shouldSelectForthQuarterWhenDifferenceXIs0AndDifferenceYIsLessThan0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.ZERO, BigDecimal.valueOf(-10));

        // then
        assertThat(result).isExactlyInstanceOf(ThirdQuarter.class);
    }

    @Test
    public void shouldSelectForthQuarterWhenDifferenceXIs0AndDifferenceYIsGreaterThan0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.ZERO, BigDecimal.TEN);

        // then
        assertThat(result).isExactlyInstanceOf(FirstQuarter.class);
    }

    @Test
    public void shouldSelectForthQuarterWhenDifferenceXIsGreaterThan0AndDifferenceYIsLessThan0() {
        // when
        final var result = QuarterStrategyBuilder.buildQuarterStrategy(BigDecimal.TEN, BigDecimal.valueOf(-10));

        // then
        assertThat(result).isExactlyInstanceOf(ForthQuarter.class);
    }
}
