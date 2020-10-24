package points.transforming.app.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import points.transforming.app.server.models.geodeticobject.GeodeticObject;

@Repository
public interface GeodeticObjectRepository extends JpaRepository<GeodeticObject, Integer> {

    List<GeodeticObject> findAllByMeasurementInternalId(String measurementInternalId);
}
