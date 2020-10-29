package points.transforming.app.server.models.measurement.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectRequest;
import points.transforming.app.server.models.picket.PicketRequest;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MeasurementRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String place;

    @NotBlank
    private final String owner;

    @NotNull
    @Positive
    private final Integer districtId;

    @Builder.Default
    private final List<PicketRequest> pickets = new ArrayList<>();

    @Builder.Default
    private final List<GeodeticObjectRequest> geodeticObjects = new ArrayList<>();
}
