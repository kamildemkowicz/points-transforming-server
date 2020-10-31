package points.transforming.app.server.models.geodeticobject.api;

import points.transforming.app.server.models.geodeticobject.GeodeticObject;
import points.transforming.app.server.models.geodeticobject.SingleLine;

public final class SingleLineMappers {

    public static SingleLine toSingleLine(final SingleLineRequest singleLineRequest, final GeodeticObject geodeticObject) {
        final var singleLine = new SingleLine();

        singleLine.setStartPicketInternalId(singleLineRequest.getStartPicketInternalId());
        singleLine.setEndPicketInternalId(singleLineRequest.getEndPicketInternalId());
        singleLine.setGeodeticObject(geodeticObject);

        return singleLine;
    }
}
