package points.transforming.app.server.models.measurement;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import points.transforming.app.server.models.picket.PicketRequest;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class MeasurementRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String place;

    @NotBlank
    private String owner;

    @NotNull
    @Positive
    private Integer districtId;

    @Builder.Default
    private List<PicketRequest> pickets = new ArrayList<>();
}
