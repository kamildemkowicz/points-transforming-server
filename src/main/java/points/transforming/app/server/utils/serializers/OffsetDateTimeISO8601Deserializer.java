package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OffsetDateTimeISO8601Deserializer extends JsonDeserializer<OffsetDateTime> {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        // date/time
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        // offset (hh:mm - "+00:00" when it's zero)
        .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
        // offset (hhmm - "+0000" when it's zero)
        .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
        // offset (hh - "Z" when it's zero)
        .optionalStart().appendOffset("+HH", "Z").optionalEnd()
        // create formatter
        .toFormatter();

    @Override
    public OffsetDateTime deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        return OffsetDateTime.parse(p.getText(), formatter);
    }
}
