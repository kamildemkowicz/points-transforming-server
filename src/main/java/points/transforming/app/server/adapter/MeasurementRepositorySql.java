package points.transforming.app.server.adapter;

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
}
