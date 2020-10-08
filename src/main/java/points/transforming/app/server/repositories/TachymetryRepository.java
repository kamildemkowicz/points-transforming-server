package points.transforming.app.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import points.transforming.app.server.models.tachymetry.Tachymetry;

@Repository
public interface TachymetryRepository extends JpaRepository<Tachymetry, Integer> {
}
