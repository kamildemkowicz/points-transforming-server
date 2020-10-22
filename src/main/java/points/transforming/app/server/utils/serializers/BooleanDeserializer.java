package points.transforming.app.server.utils.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import points.transforming.app.server.exceptions.ExceptionUtils;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        try {
            return p.getBooleanValue();
        } catch (final IOException e) {
            throw ExceptionUtils.throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable("Only boolean values are allowed",
                p.currentName(),
                p.getParsingContext().getCurrentValue().getClass(),
                Boolean.class);
        }
    }
}
