package points.transforming.app.server.services.tachymetry.quarter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import points.transforming.app.server.services.tachymetry.QuarterStrategy;
import points.transforming.app.server.services.tachymetry.TachymetryUtils;

@AllArgsConstructor
public class SecondQuarter implements QuarterStrategy {
    private final BigDecimal differenceX;
    private final BigDecimal differenceY;

    @Override
    public BigDecimal calculateAzimuth() {
        if (differenceX.equals(BigDecimal.ZERO))
            return BigDecimal.valueOf(100);

        if (differenceY.equals(BigDecimal.ZERO))
            return BigDecimal.valueOf(200);

        final var angle = differenceY.divide(differenceX, 10, RoundingMode.HALF_UP);

        final var angleInRadian = Math.abs(Math.atan(angle.doubleValue()));
        final var angleInGrad = TachymetryUtils.calculateFromRadianToGrad(angleInRadian);

        return BigDecimal.valueOf(200 - angleInGrad).setScale(4, RoundingMode.CEILING);
    }
}
