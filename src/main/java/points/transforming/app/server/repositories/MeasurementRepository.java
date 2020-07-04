package points.transforming.app.server.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import points.transforming.app.server.models.measurement.Measurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository  {
    List<Measurement> findAll();
    List<Measurement> findAllByEndDate(LocalDateTime endDate);

    Page<Measurement> findAll(Pageable page);

    Optional<Measurement> findById(Integer id);
    Optional<List<Measurement>> findByMeasurementInternalId(String id);
    Optional<Measurement> findByMeasurementInternalIdAndEndDate(String id, LocalDateTime endDate);

    Measurement save(Measurement measurement);

    int getHighestInternalId();
}
