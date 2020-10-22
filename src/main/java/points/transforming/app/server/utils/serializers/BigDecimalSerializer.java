package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import points.transforming.app.server.utils.NumberUtils;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    public void serialize(final BigDecimal value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeString(NumberUtils.amountAsString(value));
    }

}
