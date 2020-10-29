package points.transforming.app.server.models.geodeticobject.api;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeodeticObjectResponse {

    private final Integer id;
    private final String name;
    private final String description;
    private final String symbol;
    private final String color;
    private final List<SingleLineResponse> singleLines;
}
