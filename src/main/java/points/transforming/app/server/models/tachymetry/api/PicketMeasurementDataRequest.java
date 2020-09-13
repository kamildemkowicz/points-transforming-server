package points.transforming.app.server.models.tachymetry.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PicketMeasurementDataRequest {

    @NotNull
    private final String picketName;

    @NotNull
    @Positive
    private final BigDecimal distance;

    @NotNull
    @PositiveOrZero
    private final BigDecimal angle;
}
