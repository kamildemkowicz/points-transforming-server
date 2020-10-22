package points.transforming.app.server.services.measurement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.measurement.District;
import points.transforming.app.server.models.measurement.api.DistrictResponse;

public class DistrictResponseProvider {

    public ResponseEntity<List<DistrictResponse>> doResponse(final List<District> districts) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(districts.stream()
                .map(DistrictResponse::of)
                .collect(Collectors.toUnmodifiableList())
            );
    }
}
