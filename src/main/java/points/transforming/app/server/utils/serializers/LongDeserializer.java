package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import points.transforming.app.server.exceptions.ExceptionUtils;

public class LongDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        try {
            Long longValue = p.getLongValue();
            BigDecimal decimalValue = p.getDecimalValue();
            if (!longValue.toString().equals(decimalValue.toString())) {
                throw new IOException();
            }
            return p.getLongValue();
        } catch (final IOException e) {
            throw ExceptionUtils.throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable("Only Long values are allowed",
                p.currentName(),
                p.getParsingContext().getCurrentValue().getClass(),
                Long.class);
        }
    }
}
