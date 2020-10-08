package points.transforming.app.server.services.picket;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ComplexNumber {
    private final BigDecimal realNumber;
    private final BigDecimal imaginaryNumber;
}
