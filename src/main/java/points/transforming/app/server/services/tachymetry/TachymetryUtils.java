package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TachymetryUtils {
    private static final double HALF_ANGLE = 200;
    private static final double FULL_ANGLE = 360;

    public static double calculateFromRadianToGrad(final double angleInRad) {
        return angleInRad * HALF_ANGLE / Math.PI;
    }

    public static double calculateFromGradToRadian(final double angleInGrad) {
        return angleInGrad * Math.PI / HALF_ANGLE;
    }

    public static BigDecimal calculateFromRadianToDegrees(final double angleInRad) {
        return BigDecimal.valueOf(angleInRad).multiply(BigDecimal.valueOf(FULL_ANGLE)).divide(BigDecimal.valueOf(2 * Math.PI), 20, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateFromDegreesToRadian(final double angleInDegrees) {
        return BigDecimal.valueOf(angleInDegrees).multiply(BigDecimal.valueOf(2 * Math.PI)).divide(BigDecimal.valueOf(FULL_ANGLE), 20,
            RoundingMode.HALF_UP);
    }
}
