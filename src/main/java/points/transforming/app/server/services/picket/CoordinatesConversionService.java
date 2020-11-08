package points.transforming.app.server.services.picket;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.services.tachymetry.TachymetryUtils;

@Service
public class CoordinatesConversionService {

    public ConvertedCoordinates convertCoordinateFromGeocentricToWgs84(final Picket picket, final Integer zone) {
        final var xGK = calculateXGk(BigDecimal.valueOf(picket.getCoordinateX2000()));
        final var yGK = calculateYGk(BigDecimal.valueOf(picket.getCoordinateY2000()), zone);

        final var mercatorCoordinates = calculateFromGaussKrugerToMercator(xGK, yGK);
        final var lagrangeCoordinates = calculateFromMercatorToLagrange(mercatorCoordinates);
        final var wgs84Coordinates = calculateFromLagrangeToWgs84(lagrangeCoordinates, zone);

        return ConvertedCoordinates.builder()
            .coordinateX(TachymetryUtils.calculateFromRadianToDegrees(wgs84Coordinates.getCoordinateX().doubleValue()))
            .coordinateY(wgs84Coordinates.getCoordinateY())
            .build();
    }

    public ConvertedCoordinates convertCoordinateFromWgs84ToGeocentric(final Picket picket, final Integer zone) {
        final var latInRadian = TachymetryUtils.calculateFromDegreesToRadian(picket.getLatitude());
        final var longInRadian = TachymetryUtils.calculateFromDegreesToRadian(BigDecimal.valueOf(picket.getLongitude())
            .subtract(selectMeridian(zone)).doubleValue());

        final var lagrangeCoordinates = calculateFromWgs84ToLagrange(ConvertedCoordinates.builder()
            .coordinateX(latInRadian)
            .coordinateY(longInRadian)
            .build());

        final var mercatorCoordinates = calculateFromLagrangeToMercator(lagrangeCoordinates);

        final var coordinatesInGk = calculateFromMercatorToGaussKruger(mercatorCoordinates);

        return ConvertedCoordinates.builder()
            .coordinateX(calculateX2000(coordinatesInGk.getCoordinateX()).setScale(2, RoundingMode.HALF_UP))
            .coordinateY(calculateY2000(coordinatesInGk.getCoordinateY(), zone).setScale(2, RoundingMode.HALF_UP))
            .build();
    }

    private BigDecimal calculateXGk(final BigDecimal coordinateX) {
        return coordinateX.divide(ConversionConstants.MO,20, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateYGk(final BigDecimal coordinateY, final Integer zone) {
        return coordinateY.subtract(selectZone(zone)).divide(ConversionConstants.MO,20, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateX2000(final BigDecimal coordinateGkX) {
        return ConversionConstants.MO.multiply(coordinateGkX);
    }

    private BigDecimal calculateY2000(final BigDecimal coordinateGkY, final Integer zone) {
        return ConversionConstants.MO.multiply(coordinateGkY).add(selectZone(zone));
    }

    private ConvertedCoordinates calculateFromLagrangeToWgs84(final ConvertedCoordinates convertedCoordinates, final Integer zone) {
        final var fi = convertedCoordinates.getCoordinateX();

        final var fi2 = Math.sin(fi.multiply(BigDecimal.valueOf(2)).doubleValue());
        final var c2fi2 = ConversionConstants.C2.multiply(BigDecimal.valueOf(fi2));

        final var fi4 = Math.sin(fi.multiply(BigDecimal.valueOf(4)).doubleValue());
        final var c4fi4 = ConversionConstants.C4.multiply(BigDecimal.valueOf(fi4));

        final var fi6 = Math.sin(fi.multiply(BigDecimal.valueOf(6)).doubleValue());
        final var c6fi6 = ConversionConstants.C6.multiply(BigDecimal.valueOf(fi6));

        final var fi8 = Math.sin(fi.multiply(BigDecimal.valueOf(8)).doubleValue());
        final var c8fi8 = ConversionConstants.C8.multiply(BigDecimal.valueOf(fi8));

        final var b = fi.add(c2fi2).add(c4fi4).add(c6fi6).add(c8fi8);
        final var l0 = TachymetryUtils.calculateFromRadianToDegrees(convertedCoordinates.getCoordinateY().doubleValue());

        return ConvertedCoordinates.builder()
            .coordinateX(b)
            .coordinateY(selectMeridian(zone).add(l0))
            .build();
    }

    private ConvertedCoordinates calculateFromWgs84ToLagrange(final ConvertedCoordinates convertedCoordinates) {
        final var eSinB = BigDecimal.valueOf(Math.sin(convertedCoordinates.getCoordinateX().doubleValue())).multiply(ConversionConstants.E);
        final var u = BigDecimal.ONE.subtract(eSinB);
        final var v = BigDecimal.ONE.add(eSinB);

        final var e2 = ConversionConstants.E.divide(BigDecimal.valueOf(2), 20, RoundingMode.HALF_UP);
        final var k = BigDecimal.valueOf(Math.pow(u.divide(v, 20, RoundingMode.HALF_UP).doubleValue(), e2.doubleValue()));
        final var tanB = Math.tan((convertedCoordinates.getCoordinateX().divide(BigDecimal.valueOf(2), 20, RoundingMode.HALF_UP).doubleValue())
            + (BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(4), 20, RoundingMode.HALF_UP).doubleValue()));
        final var c = k.multiply(BigDecimal.valueOf(tanB));

        final var arctgC = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Math.atan(c.doubleValue())));
        final var pi2 = BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(2), 20, RoundingMode.HALF_UP);

        final var fi = arctgC.subtract(pi2);

        return ConvertedCoordinates.builder()
            .coordinateX(fi)
            .coordinateY(convertedCoordinates.getCoordinateY())
            .build();
    }

    private ConvertedCoordinates calculateFromMercatorToLagrange(final ConvertedCoordinates mercatorCoordinates) {
        final var alpha = mercatorCoordinates.getCoordinateX().divide(ConversionConstants.R0, 20, RoundingMode.HALF_UP);
        final var beta = mercatorCoordinates.getCoordinateY().divide(ConversionConstants.R0, 20, RoundingMode.HALF_UP);

        final var w1 = BigDecimal.valueOf((2 * Math.atan(Math.exp(beta.doubleValue()))));
        final var w = w1.subtract(BigDecimal.valueOf(Math.PI/2));

        final var fi = Math.asin(Math.cos(w.doubleValue()) * Math.sin(alpha.doubleValue()));
        final var deltaLambda = Math.atan(BigDecimal.valueOf(Math.tan(w.doubleValue())).divide(BigDecimal.valueOf(Math.cos(alpha.doubleValue())),
            30, RoundingMode.HALF_UP).doubleValue());

        return ConvertedCoordinates.builder()
            .coordinateX(BigDecimal.valueOf(fi))
            .coordinateY(BigDecimal.valueOf(deltaLambda))
            .build();

    }

    private ConvertedCoordinates calculateFromLagrangeToMercator(final ConvertedCoordinates lagrangeCoordinates) {
        final var p = BigDecimal.valueOf(Math.sin(lagrangeCoordinates.getCoordinateX().doubleValue()));
        final var fiCos = BigDecimal.valueOf(Math.cos(lagrangeCoordinates.getCoordinateX().doubleValue()));
        final var lambdaCos = BigDecimal.valueOf(Math.cos(lagrangeCoordinates.getCoordinateY().doubleValue()));
        final var lambdaSin = BigDecimal.valueOf(Math.sin(lagrangeCoordinates.getCoordinateY().doubleValue()));
        final var lambdaSinFiCos = fiCos.multiply(lambdaSin);
        final var q = fiCos.multiply(lambdaCos);
        final var r = lambdaSinFiCos.add(BigDecimal.ONE);
        final var s = BigDecimal.ONE.subtract(lambdaSinFiCos);

        final var xMerc = BigDecimal.valueOf(Math.atan(p.divide(q, 20, RoundingMode.HALF_UP).doubleValue())).multiply(ConversionConstants.R0);
        final var yMerc = BigDecimal.valueOf(Math.log(r.divide(s, 20, RoundingMode.HALF_UP).doubleValue())).multiply(ConversionConstants.R0).multiply(BigDecimal.valueOf(0.5));

        return ConvertedCoordinates.builder()
            .coordinateX(xMerc)
            .coordinateY(yMerc)
            .build();
    }

    private ConvertedCoordinates calculateFromGaussKrugerToMercator(final BigDecimal xGk, final BigDecimal yGk) {
        var zX = xGk.subtract(ConversionConstants.AO).multiply(ConversionConstants.S);
        var zY = yGk.multiply(ConversionConstants.S);

        final var z = ComplexNumber.builder()
            .realNumber(zX)
            .imaginaryNumber(zY)
            .build();

        final var z6 = calculateMultiplyingComplexNumbers(z, ComplexNumber.builder()
            .realNumber(ConversionConstants.B6)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z6b5 = calculateComplexNumbersSum(z6, ComplexNumber.builder()
            .realNumber(ConversionConstants.B5)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z5 = calculateMultiplyingComplexNumbers(z, z6b5);

        final var z5b4 = calculateComplexNumbersSum(z5, ComplexNumber.builder()
            .realNumber(ConversionConstants.B4)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z4 = calculateMultiplyingComplexNumbers(z, z5b4);

        final var z4b3 = calculateComplexNumbersSum(z4, ComplexNumber.builder()
            .realNumber(ConversionConstants.B3)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z3 = calculateMultiplyingComplexNumbers(z, z4b3);

        final var z3b2 = calculateComplexNumbersSum(z3, ComplexNumber.builder()
            .realNumber(ConversionConstants.B2)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z2 = calculateMultiplyingComplexNumbers(z, z3b2);

        final var z2b1 = calculateComplexNumbersSum(z2, ComplexNumber.builder()
            .realNumber(ConversionConstants.B1)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z1 = calculateMultiplyingComplexNumbers(z, z2b1);

        final var zMerc = calculateComplexNumbersSum(z1, ComplexNumber.builder()
            .realNumber(ConversionConstants.B0)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        return ConvertedCoordinates.builder()
            .coordinateX(zMerc.getRealNumber())
            .coordinateY(zMerc.getImaginaryNumber())
            .build();
    }

    private ConvertedCoordinates calculateFromMercatorToGaussKruger(final ConvertedCoordinates mercatorCoordinates) {

        final var z = ComplexNumber.builder()
            .realNumber((mercatorCoordinates.getCoordinateX().subtract(ConversionConstants.XO)).multiply(ConversionConstants.S))
            .imaginaryNumber(mercatorCoordinates.getCoordinateY().multiply(ConversionConstants.S))
            .build();

        final var z6 = calculateMultiplyingComplexNumbers(z, ComplexNumber.builder()
            .realNumber(ConversionConstants.A6)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z6a5 = calculateComplexNumbersSum(z6, ComplexNumber.builder()
            .realNumber(ConversionConstants.A5)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z5 = calculateMultiplyingComplexNumbers(z, z6a5);

        final var z5a4 = calculateComplexNumbersSum(z5, ComplexNumber.builder()
            .realNumber(ConversionConstants.A4)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z4 = calculateMultiplyingComplexNumbers(z, z5a4);

        final var z4a3 = calculateComplexNumbersSum(z4, ComplexNumber.builder()
            .realNumber(ConversionConstants.A3)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z3 = calculateMultiplyingComplexNumbers(z, z4a3);

        final var z3a2 = calculateComplexNumbersSum(z3, ComplexNumber.builder()
            .realNumber(ConversionConstants.A2)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z2 = calculateMultiplyingComplexNumbers(z, z3a2);

        final var z2a1 = calculateComplexNumbersSum(z2, ComplexNumber.builder()
            .realNumber(ConversionConstants.A1)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        final var z1 = calculateMultiplyingComplexNumbers(z, z2a1);

        final var coordinatesGk = calculateComplexNumbersSum(z1, ComplexNumber.builder()
            .realNumber(ConversionConstants.A0)
            .imaginaryNumber(BigDecimal.ZERO)
            .build());

        return ConvertedCoordinates.builder()
            .coordinateX(coordinatesGk.getRealNumber())
            .coordinateY(coordinatesGk.getImaginaryNumber())
            .build();
    }

    private ComplexNumber calculateComplexNumbersSum(final ComplexNumber mercatorCoordinates, final ComplexNumber complexNumber) {
        return ComplexNumber.builder()
            .realNumber(mercatorCoordinates.getRealNumber().add(complexNumber.getRealNumber()))
            .imaginaryNumber(mercatorCoordinates.getImaginaryNumber().add(complexNumber.getImaginaryNumber()))
            .build();
    }

    private ComplexNumber calculateMultiplyingComplexNumbers(final ComplexNumber mercatorCoordinates, final ComplexNumber complexNumber) {
        final var x1x2 = mercatorCoordinates.getRealNumber().multiply(complexNumber.getRealNumber());
        final var y1y2 = mercatorCoordinates.getImaginaryNumber().multiply(complexNumber.getImaginaryNumber());

        final var x1y2 = mercatorCoordinates.getRealNumber().multiply(complexNumber.getImaginaryNumber());
        final var x2y1 = complexNumber.getRealNumber().multiply(mercatorCoordinates.getImaginaryNumber());

        return ComplexNumber.builder()
            .realNumber(x1x2.subtract(y1y2))
            .imaginaryNumber(x1y2.add(x2y1))
            .build();
    }

    private BigDecimal selectZone(final Integer zone) {
        switch (zone) {
            case 5:
                return ConversionConstants.YO_15;
            case 6:
                return ConversionConstants.YO_18;
            case 7:
                return ConversionConstants.YO_21;
            default:
                return ConversionConstants.YO_24;
        }
    }

    private BigDecimal selectMeridian(final Integer zone) {
        switch (zone) {
            case 5:
                return ConversionConstants.z15;
            case 6:
                return ConversionConstants.z18;
            case 7:
                return ConversionConstants.z21;
            default:
                return ConversionConstants.z24;
        }
    }
}
