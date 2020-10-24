package points.transforming.app.server.models.measurement.api;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.PicketResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MeasurementResponse {
    private final int id;
    private final String name;
    private final String measurementInternalId;
    private final String place;
    private final String owner;
    private final LocalDateTime creationDate;
    private final DistrictResponse district;
    private final List<PicketResponse> pickets;

    public static MeasurementResponse of(final Measurement measurement) {
        return MeasurementResponse.builder()
            .id(measurement.getId())
            .measurementInternalId(measurement.getMeasurementInternalId())
            .name(measurement.getName())
            .owner(measurement.getOwner())
            .place(measurement.getPlace())
            .creationDate(measurement.getCreationDate())
            .district(DistrictResponse.of(measurement.getDistrict()))
            .pickets(measurement.getPickets().stream()
                .map(PicketResponse::of)
                .collect(Collectors.toUnmodifiableList())
            )
            .build();
    }
}
