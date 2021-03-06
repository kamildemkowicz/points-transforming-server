package points.transforming.app.server.models.tachymetry.api;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.picket.Picket;

@Getter
@Builder
public class PicketResponse {
    private final int id;
    private final String picketInternalId;
    private final String name;
    private final double longitude;
    private final double latitude;
    private final double coordinateX2000;
    private final double coordinateY2000;

    public static PicketResponse of(final Picket picket) {
        return PicketResponse.builder()
            .picketInternalId(picket.getPicketInternalId())
            .name(picket.getName())
            .longitude(picket.getLongitude())
            .latitude(picket.getLatitude())
            .coordinateX2000(picket.getCoordinateX2000())
            .coordinateY2000(picket.getCoordinateY2000())
            .build();
    }
}
