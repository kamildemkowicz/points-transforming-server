package points.transforming.app.server.services.tachymetry;

import java.math.BigDecimal;

import points.transforming.app.server.services.tachymetry.quarter.FirstQuarter;
import points.transforming.app.server.services.tachymetry.quarter.ForthQuarter;
import points.transforming.app.server.services.tachymetry.quarter.SecondQuarter;
import points.transforming.app.server.services.tachymetry.quarter.ThirdQuarter;

public class QuarterStrategyBuilder {

    public static QuarterStrategy buildQuarterStrategy(final BigDecimal differenceX, final BigDecimal differenceY) {
        if (differenceX.compareTo(BigDecimal.ZERO) >= 0 && differenceY.compareTo(BigDecimal.ZERO) >= 0)
            return new FirstQuarter(differenceX, differenceY);

        if (differenceX.compareTo(BigDecimal.ZERO) <= 0 && differenceY.compareTo(BigDecimal.ZERO) >= 0)
            return new SecondQuarter(differenceX, differenceY);

        if (differenceX.compareTo(BigDecimal.ZERO) <= 0 && differenceY.compareTo(BigDecimal.ZERO) <= 0)
            return new ThirdQuarter(differenceX, differenceY);

        return new ForthQuarter(differenceX, differenceY);
    }
}
