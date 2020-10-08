package points.transforming.app.server.controllers;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.models.tachymetry.api.TachymetryResponse;
import points.transforming.app.server.services.tachymetry.TachymetryResponseProvider;
import points.transforming.app.server.services.tachymetry.TachymetryService;

@RestController
@RequestMapping("tachymetry")
@AllArgsConstructor
@TachymetryExceptionProcessing
@MeasurementExceptionProcessing
public class TachymetryController {

    private final TachymetryService tachymetryService;

    @PostMapping
    public ResponseEntity<TachymetryResponse> calculateTachymetry(@RequestBody @Valid final TachymetryRequest tachymetryRequest) {
        return new TachymetryResponseProvider().doResponse(
            tachymetryRequest.getInternalMeasurementId(),
            tachymetryService.calculateTachymetry(tachymetryRequest),
            tachymetryRequest.getTachymetryMetaData()
        );
    }
}
