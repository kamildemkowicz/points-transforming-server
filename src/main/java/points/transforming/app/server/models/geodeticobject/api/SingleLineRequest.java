package points.transforming.app.server.models.geodeticobject.api;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SingleLineRequest {

    @NotBlank
    private final String startPicketInternalId;

    @NotBlank
    private final String endPicketInternalId;
}
