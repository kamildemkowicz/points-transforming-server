package points.transforming.app.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import points.transforming.app.server.models.Measurement;

@Repository
public interface MeasurementRepositorySql extends MeasurementRepository, JpaRepository<Measurement, Integer> { }
