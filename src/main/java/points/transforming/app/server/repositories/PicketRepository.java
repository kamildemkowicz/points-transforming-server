package points.transforming.app.server.repositories;

import java.util.List;

import points.transforming.app.server.models.picket.Picket;

public interface PicketRepository {
    int getHighestInternalId();

    List<Picket> saveAll(List<Picket> pickets);
}
