package points.transforming.app.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import points.transforming.app.server.models.picket.Picket;

public interface PicketRepository extends JpaRepository<Picket, Integer>  {

    @Query(nativeQuery = true, value = "select max(CAST(REPLACE(picket_internal_id, \"PIC-\", \"\") AS SIGNED)) from picket")
    int getHighestInternalId();

    @Query(nativeQuery = true, value = "select * from picket WHERE picket_internal_id = :picketInternalId AND measurement_id in (SELECT MAX(measurement_id) FROM picket WHERE picket_internal_id = :picketInternalId)")
    Picket getByPicketInternalId(String picketInternalId);
}
