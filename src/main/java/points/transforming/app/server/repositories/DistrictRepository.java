package points.transforming.app.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import points.transforming.app.server.models.measurement.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
}
