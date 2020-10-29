package points.transforming.app.server.controllers;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectResponse;
import points.transforming.app.server.services.geodeticobject.GeodeticObjectResponseProvider;
import points.transforming.app.server.services.measurement.GeodeticObjectService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "geodeticObjects")
public class GeodeticObjectController {

    private final GeodeticObjectService geodeticObjectService;

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<List<GeodeticObjectResponse>> getGeodeticObjects(@PathVariable final String measurementInternalId) {
        return new GeodeticObjectResponseProvider().doResponse(geodeticObjectService.getGeodeticObjects(measurementInternalId));
    }

    @DeleteMapping(value = "/{geodeticObjectId}")
    public ResponseEntity<String> deleteGeodeticObjects(@PathVariable final Integer geodeticObjectId) {
        geodeticObjectService.deleteGeodeticObject(geodeticObjectId);

        return ResponseEntity.ok("Removed");
    }
}
