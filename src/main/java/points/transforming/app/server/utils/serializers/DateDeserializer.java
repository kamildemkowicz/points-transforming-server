package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        return LocalDate.parse(p.getText(), DateTimeFormatter.ISO_DATE);
    }
}
