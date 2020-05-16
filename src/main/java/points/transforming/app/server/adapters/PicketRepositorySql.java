package points.transforming.app.server.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.repositories.PicketRepository;

public interface PicketRepositorySql extends PicketRepository, JpaRepository<Picket, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select max(CAST(REPLACE(picket_internal_id, \"PIC-\", \"\") AS SIGNED)) from picket")
    int getHighestInternalId();
}
