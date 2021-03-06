package points.transforming.app.server.models.picket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PicketResponse {
    private final int id;
    private final String picketInternalId;
    private final String name;
    private final Double longitude;
    private final Double latitude;
    private final Double coordinateX2000;
    private final Double coordinateY2000;

    public static PicketResponse of(final Picket picket) {
        return PicketResponse.builder()
            .id(picket.getId())
            .picketInternalId(picket.getPicketInternalId())
            .name(picket.getName())
            .longitude(picket.getLongitude())
            .latitude(picket.getLatitude())
            .coordinateX2000(picket.getCoordinateX2000())
            .coordinateY2000(picket.getCoordinateY2000())
            .build();
    }
}
