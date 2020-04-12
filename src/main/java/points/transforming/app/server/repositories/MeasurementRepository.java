package points.transforming.app.server.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import points.transforming.app.server.models.Measurement;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository  {
    List<Measurement> findAll();

    Page<Measurement> findAll(Pageable page);

    Optional<Measurement> findById(Integer id);

    Measurement save(Measurement measurement);
}
