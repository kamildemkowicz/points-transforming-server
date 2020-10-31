package points.transforming.app.server.models.geodeticobject.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SingleLineUpdateRequest {

    @NotNull
    private final Integer id;

    @NotBlank
    private final String startPicketInternalId;

    @NotBlank
    private final String endPicketInternalId;
}
