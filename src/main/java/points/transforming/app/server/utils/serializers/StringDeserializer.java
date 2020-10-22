package points.transforming.app.server.utils.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import points.transforming.app.server.exceptions.ExceptionUtils;

public class StringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            return p.getValueAsString();
        }
        throw ExceptionUtils.throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable("Value is not a string", p.currentName(),
            p.getParsingContext().getCurrentValue().getClass(), String.class);
    }
}
