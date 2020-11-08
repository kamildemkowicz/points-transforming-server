package points.transforming.app.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    Optional<Measurement> findById(Integer id);

    Optional<List<Measurement>> findByMeasurementInternalId(String id);

    Optional<Measurement> findByMeasurementInternalIdAndEndDate(String id, LocalDateTime endDate);

    Optional<Measurement> findByMeasurementInternalIdAndEndDateAndUser(String id, LocalDateTime endDate, User user);

    Measurement save(Measurement measurement);

    @Query("select distinct m from Measurement m left join fetch m.pickets WHERE m.user=:user AND m.endDate=null ORDER BY m.creationDate DESC")
    List<Measurement> findAll(final User user);

    @Query(nativeQuery = true, value = "select max(CAST(REPLACE(measurement_internal_id, \"MES-\", \"\") AS SIGNED)) from measurement")
    int getHighestInternalId();
}
