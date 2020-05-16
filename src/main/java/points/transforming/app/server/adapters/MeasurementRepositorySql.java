package points.transforming.app.server.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.repositories.MeasurementRepository;

import java.util.List;

@Repository
public interface MeasurementRepositorySql extends MeasurementRepository, JpaRepository<Measurement, Integer> {
    @Override
    @Query("select distinct m from Measurement m left join fetch m.pickets")
    List<Measurement> findAll();

    @Override
    @Query(nativeQuery = true, value = "select max(CAST(REPLACE(measurement_internal_id, \"MES-\", \"\") AS SIGNED)) from measurement")
    int getHighestInternalId();
}
