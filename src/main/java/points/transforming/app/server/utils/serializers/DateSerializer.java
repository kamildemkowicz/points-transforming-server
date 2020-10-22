package points.transforming.app.server.utils.serializers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(final LocalDate date, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
        if (Objects.isNull(date)) {
            return;
        }
        gen.writeString(DateTimeFormatter.ISO_DATE.format(date));
    }
}
