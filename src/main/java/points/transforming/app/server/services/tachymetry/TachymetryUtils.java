package points.transforming.app.server.services.tachymetry;

public class TachymetryUtils {
    private static final double HALF_ANGLE = 200;

    public static double calculateFromRadianToGrad(final double angleInRad) {
        return angleInRad * HALF_ANGLE / Math.PI;
    }

    public static double calculateFromGradToRadian(final double angleInGrad) {
        return angleInGrad * Math.PI / HALF_ANGLE;
    }
}
