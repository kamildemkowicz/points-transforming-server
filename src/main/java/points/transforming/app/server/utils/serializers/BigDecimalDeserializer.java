package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import points.transforming.app.server.exceptions.ExceptionUtils;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        try {
            return p.getDecimalValue();
        } catch (final IOException e) {
            throw ExceptionUtils.throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable("Only BigDecimal values are allowed",
                p.currentName(),
                p.getParsingContext().getCurrentValue().getClass(),
                BigDecimal.class);
        }
    }
}
