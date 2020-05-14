package points.transforming.app.server.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.repositories.PicketRepository;

public interface PicketRepositorySql extends PicketRepository, JpaRepository<Picket, Integer> { }
