package points.transforming.app.server.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class NumberUtils {

    public static String amountAsPlainString(final BigDecimal amount) {
        return (amount == null) ? null : amount.toPlainString();
    }

    public static String amountAsString(final BigDecimal amount) {
        return (amount == null) ? null : amount.stripTrailingZeros().toPlainString();
    }

    public static BigDecimal amountScaled(final BigDecimal amount, final int scale) {
        return (amount == null || scale < 0) ? null : amount.setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal notNullAmount(final BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    public static BigDecimal divide(final BigDecimal dividend, final BigDecimal divisor) {
        return dividend.divide(divisor, 4, RoundingMode.HALF_DOWN);
    }

    public static BigDecimal amountAsMinorUnits(final BigDecimal amount, final BigDecimal currencyUnitMultiplier) {
        if (amount == null || currencyUnitMultiplier == null) {
            log.warn("amountAsMinorUnits: amount or currencyUnitMultiplier is null");
            return BigDecimal.ZERO;
        }
        return amountScaled(amount.multiply(currencyUnitMultiplier), 0);
    }

    public static BigDecimal minorUnitsToAmount(final Long amount, final BigDecimal currencyUnitMultiplier) {
        if (amount == null || currencyUnitMultiplier == null) {
            log.warn("minorUnitsToAmount: amount or currencyUnitMultiplier is null");
            return BigDecimal.ZERO;
        }

        return new BigDecimal(amount).divide(currencyUnitMultiplier);
    }

    public static String scaledAmountAsString(final BigDecimal amount, final int scale) {
        return amount == null ? BigDecimal.ZERO.setScale(scale, RoundingMode.UNNECESSARY).toPlainString() : scaleAsString(amount, scale);
    }

    public static String scaledAmountAsStringOrNull(final BigDecimal amount, final int scale) {
        return amount == null ? null : scaleAsString(amount, scale);
    }

    private static String scaleAsString(final BigDecimal amount, final int scale) {
        final var noTrailingZerosAmount = amount.stripTrailingZeros();

        if (noTrailingZerosAmount.scale() >= scale) {
            return noTrailingZerosAmount.toPlainString();
        }
        return amount.setScale(scale, RoundingMode.UNNECESSARY).toPlainString();
    }
}
