package points.transforming.app.server.services.geodeticobject;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectResponse;

public class GeodeticObjectResponseProvider {

    public ResponseEntity<List<GeodeticObjectResponse>> doResponse(final List<GeodeticObjectResponse> geodeticObjects) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(geodeticObjects);
    }
}
