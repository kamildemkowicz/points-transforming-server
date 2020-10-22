package points.transforming.app.server.utils.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import points.transforming.app.server.exceptions.ExceptionUtils;

public class DoubleDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        try {
            return p.getDoubleValue();
        } catch (final IOException e) {
            throw ExceptionUtils.throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable("Only Double values are allowed",
                p.currentName(),
                p.getParsingContext().getCurrentValue().getClass(),
                Double.class);
        }
    }
}
