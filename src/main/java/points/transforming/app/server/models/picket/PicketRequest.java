package points.transforming.app.server.models.picket;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PicketRequest {
    @NotBlank
    private final String name;

    private final double longitude;
    private final double latitude;

    @Positive
    private final double coordinateX2000;
    @Positive
    private final double coordinateY2000;

    private final String picketInternalId;
}
