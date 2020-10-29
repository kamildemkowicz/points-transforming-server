package points.transforming.app.server.models.geodeticobject.api;

import java.util.Objects;
import java.util.stream.Collectors;

import points.transforming.app.server.models.geodeticobject.GeodeticObject;

public final class GeodeticObjectMappers {

    public static GeodeticObject toGeodeticObject(final GeodeticObjectRequest geodeticObjectRequest, final String measurementInternalId) {
        final var geodeticObject = new GeodeticObject();
        if (Objects.nonNull(geodeticObjectRequest.getId())) {
            geodeticObject.setId(geodeticObjectRequest.getId());
        }
        geodeticObject.setName(geodeticObjectRequest.getName());
        geodeticObject.setDescription(geodeticObjectRequest.getDescription());
        geodeticObject.setSymbol(geodeticObjectRequest.getSymbol());
        geodeticObject.setColor(geodeticObjectRequest.getColor());
        geodeticObject.setMeasurementInternalId(measurementInternalId);
        geodeticObject.setSingleLines(geodeticObjectRequest.getSingleLines()
            .stream()
            .map(singleLine -> SingleLineMappers.toSingleLine(singleLine, geodeticObject))
            .collect(Collectors.toUnmodifiableList())
        );

        return geodeticObject;
    }
}
