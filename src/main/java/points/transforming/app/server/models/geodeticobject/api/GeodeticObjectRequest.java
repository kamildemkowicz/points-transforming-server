package points.transforming.app.server.models.geodeticobject.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeodeticObjectRequest {
    @NotBlank
    private final String measurementInternalId;

    @NotBlank
    private final String name;

    private final String description;

    @NotBlank
    private final String symbol;

    @NotBlank
    private final String color;

    @NotEmpty
    private final List<SingleLineRequest> singleLines;
}
