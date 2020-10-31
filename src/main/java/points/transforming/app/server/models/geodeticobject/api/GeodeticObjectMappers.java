package points.transforming.app.server.models.geodeticobject.api;

import java.util.stream.Collectors;

import points.transforming.app.server.models.geodeticobject.GeodeticObject;

public final class GeodeticObjectMappers {

    public static GeodeticObject toGeodeticObject(final GeodeticObjectRequest geodeticObjectRequest) {
        final var geodeticObject = new GeodeticObject();

        geodeticObject.setName(geodeticObjectRequest.getName());
        geodeticObject.setDescription(geodeticObjectRequest.getDescription());
        geodeticObject.setSymbol(geodeticObjectRequest.getSymbol());
        geodeticObject.setColor(geodeticObjectRequest.getColor());
        geodeticObject.setMeasurementInternalId(geodeticObjectRequest.getMeasurementInternalId());
        geodeticObject.setSingleLines(geodeticObjectRequest.getSingleLines()
            .stream()
            .map(singleLine -> SingleLineMappers.toSingleLine(singleLine, geodeticObject))
            .collect(Collectors.toUnmodifiableList())
        );

        return geodeticObject;
    }

    public static GeodeticObject updateGeodeticObject(final GeodeticObject geodeticObjectToUpdate,
                                                      final GeodeticObjectUpdateRequest geodeticObjectUpdateRequest) {
        geodeticObjectToUpdate.setSymbol(geodeticObjectUpdateRequest.getSymbol());
        geodeticObjectToUpdate.setColor(geodeticObjectUpdateRequest.getColor());
        geodeticObjectToUpdate.setDescription(geodeticObjectUpdateRequest.getDescription());
        geodeticObjectToUpdate.setName(geodeticObjectUpdateRequest.getName());

        return geodeticObjectToUpdate;
    }
}
