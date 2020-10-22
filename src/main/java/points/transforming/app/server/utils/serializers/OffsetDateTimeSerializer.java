package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

    @Override
    public void serialize(final OffsetDateTime value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        if (Objects.isNull(value)) {
            return;
        }
        gen.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value.toInstant().atOffset(ZoneOffset.UTC)));
    }
}
