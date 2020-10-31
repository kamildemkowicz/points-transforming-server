package points.transforming.app.server.controllers;

import javax.validation.Valid;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.geodeticobject.api.DeleteGeodeticObjectResponse;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectRequest;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectResponse;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectUpdateRequest;
import points.transforming.app.server.services.geodeticobject.GeodeticObjectResponseProvider;
import points.transforming.app.server.services.measurement.GeodeticObjectService;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(path = "geodeticObjects")
public class GeodeticObjectController {

    private final GeodeticObjectService geodeticObjectService;

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<List<GeodeticObjectResponse>> getGeodeticObjects(@PathVariable final String measurementInternalId) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.getGeodeticObjects(measurementInternalId));
    }

    @PostMapping
    public ResponseEntity<GeodeticObjectResponse> createGeodeticObject(@RequestBody @Valid final GeodeticObjectRequest geodeticObjectRequest) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.createGeodeticObject(geodeticObjectRequest));
    }

    @PutMapping
    public ResponseEntity<GeodeticObjectResponse> updateGeodeticObject(@RequestBody @Valid final GeodeticObjectUpdateRequest geodeticObjectUpdateRequest) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.updateGeodeticObject(geodeticObjectUpdateRequest));
    }

    @DeleteMapping(value = "/{geodeticObjectId}")
    public ResponseEntity<DeleteGeodeticObjectResponse> deleteGeodeticObjects(@PathVariable final Integer geodeticObjectId) {
        geodeticObjectService.deleteGeodeticObject(geodeticObjectId);

        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectId);
    }
}
