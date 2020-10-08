package points.transforming.app.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import points.transforming.app.server.models.tachymetry.MeasuringStation;

@Repository
public interface MeasuringStationRepository extends JpaRepository<MeasuringStation, Integer> {
}
