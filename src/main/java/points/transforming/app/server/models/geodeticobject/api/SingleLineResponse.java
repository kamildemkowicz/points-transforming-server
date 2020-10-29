package points.transforming.app.server.models.geodeticobject.api;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.geodeticobject.SingleLine;
import points.transforming.app.server.models.picket.PicketResponse;

@Getter
@Builder
public class SingleLineResponse {

    private final Integer id;
    private final PicketResponse startPicket;
    private final PicketResponse endPicket;

    public static SingleLineResponse of(final SingleLine singleLine, final PicketResponse startPicket, final PicketResponse endPicket) {
        return SingleLineResponse.builder()
            .id(singleLine.getId())
            .startPicket(startPicket)
            .endPicket(endPicket)
            .build();
    }
}
