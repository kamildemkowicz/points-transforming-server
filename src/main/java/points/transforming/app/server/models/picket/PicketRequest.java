package points.transforming.app.server.models.picket;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PicketRequest {
    @NotBlank
    private final String name;

    private final Double longitude;
    private final Double latitude;

    @NotNull
    @Positive
    private final Double coordinateX2000;

    @NotNull
    @Positive
    private final Double coordinateY2000;

    private final String picketInternalId;
}
