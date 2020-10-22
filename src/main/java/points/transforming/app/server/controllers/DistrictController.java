package points.transforming.app.server.controllers;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.measurement.api.DistrictResponse;
import points.transforming.app.server.services.measurement.DistrictResponseProvider;
import points.transforming.app.server.services.measurement.DistrictService;

@RestController
@RequestMapping("districts")
@AllArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    public ResponseEntity<List<DistrictResponse>> getDistricts() {
        return new DistrictResponseProvider().doResponse(districtService.getAllDistricts());
    }
}
