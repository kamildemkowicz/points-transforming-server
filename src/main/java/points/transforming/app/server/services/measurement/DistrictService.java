package points.transforming.app.server.services.measurement;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import points.transforming.app.server.exceptions.district.DistrictNotFoundException;
import points.transforming.app.server.models.measurement.District;
import points.transforming.app.server.repositories.DistrictRepository;

import static points.transforming.app.server.services.measurement.DistrictService.Error.DISTRICT_DOES_NOT_EXIST_PTS200;

@Service
@AllArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;

    public List<District> getAllDistricts() {
        return this.districtRepository.findAll(Sort.by("name"));
    }

    public District getDistrictById(final Integer id) {
        return this.districtRepository.findById(id).orElseThrow(() -> new DistrictNotFoundException(DISTRICT_DOES_NOT_EXIST_PTS200));
    }

    enum Error {
        DISTRICT_DOES_NOT_EXIST_PTS200
    }
}
