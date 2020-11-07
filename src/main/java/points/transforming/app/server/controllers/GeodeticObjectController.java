package points.transforming.app.server.controllers;

import javax.validation.Valid;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<GeodeticObjectResponse>> getGeodeticObjects(@PathVariable final String measurementInternalId) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.getGeodeticObjects(measurementInternalId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GeodeticObjectResponse> createGeodeticObject(@RequestBody @Valid final GeodeticObjectRequest geodeticObjectRequest) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.createGeodeticObject(geodeticObjectRequest));
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GeodeticObjectResponse> updateGeodeticObject(@RequestBody @Valid final GeodeticObjectUpdateRequest geodeticObjectUpdateRequest) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.updateGeodeticObject(geodeticObjectUpdateRequest));
    }

    @DeleteMapping(value = "/{geodeticObjectId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DeleteGeodeticObjectResponse> deleteGeodeticObjects(@PathVariable final Integer geodeticObjectId) {
        geodeticObjectService.deleteGeodeticObject(geodeticObjectId);

        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectId);
    }
}
