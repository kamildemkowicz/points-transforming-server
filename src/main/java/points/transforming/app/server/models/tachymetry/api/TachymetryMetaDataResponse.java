package points.transforming.app.server.models.tachymetry.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TachymetryMetaDataResponse {
    private final String tachymetrName;
    private final String tachymetrType;
    private final Double temperature;
    private final Long pressure;
}
