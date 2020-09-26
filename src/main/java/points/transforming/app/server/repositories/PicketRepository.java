package points.transforming.app.server.repositories;

import java.util.List;

import points.transforming.app.server.models.picket.Picket;

public interface PicketRepository {
    int getHighestInternalId();

    List<Picket> save(List<Picket> pickets);
}
