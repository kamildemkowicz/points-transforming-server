package points.transforming.app.server.utils.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import points.transforming.app.server.exceptions.ExceptionUtils;

public class IntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        try {
            final var isFloatAsIntDisabled = !DeserializationFeature.ACCEPT_FLOAT_AS_INT.enabledIn(ctxt.getDeserializationFeatures());
            final var typeToken = p.getCurrentToken();

            if (isFloatAsIntDisabled && JsonToken.VALUE_NUMBER_FLOAT.equals(typeToken)) {
                throw new JsonParseException(p, "Parsing float as int is disabled");
            }
            return p.getIntValue();
        } catch (final IOException e) {
            throw ExceptionUtils.throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable("Only Integer values are allowed",
                p.currentName(),
                Integer.class,
                Integer.class);
        }
    }
}
