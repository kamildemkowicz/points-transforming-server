package points.transforming.app.server.services.geodeticobject;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.models.geodeticobject.api.DeleteGeodeticObjectResponse;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectResponse;

public class GeodeticObjectResponseProvider {

    public ResponseEntity<List<GeodeticObjectResponse>> doResponse(final List<GeodeticObjectResponse> geodeticObjects) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(geodeticObjects);
    }

    public ResponseEntity<GeodeticObjectResponse> doResponse(final GeodeticObjectResponse geodeticObject) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(geodeticObject);
    }

    public ResponseEntity<DeleteGeodeticObjectResponse> doResponse(final Integer removedGeodeticObjectId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(DeleteGeodeticObjectResponse.builder().id(removedGeodeticObjectId).build());
    }
}
