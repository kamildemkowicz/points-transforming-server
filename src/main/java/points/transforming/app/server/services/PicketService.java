package points.transforming.app.server.services;

import org.springframework.stereotype.Service;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.picket.PicketWriteModel;
import points.transforming.app.server.repositories.PicketRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PicketService {
    private final PicketRepository picketRepository;

    public PicketService(PicketRepository picketRepository) {
        this.picketRepository = picketRepository;
    }

    public void setPicketInternalIds(List<PicketWriteModel> picketsToSave) {
        var highestMeasurementInternalId = new AtomicInteger(this.picketRepository.getHighestInternalId());
        picketsToSave.forEach(picket -> picket.setPicketInternalId("PIC-" + (highestMeasurementInternalId.incrementAndGet())));
    }

    public void setInternalIdsForNewPickets(List<Picket> pickets) {
        var highestMeasurementInternalId = new AtomicInteger(this.picketRepository.getHighestInternalId());

        pickets.forEach(picket -> {
            if (picket.getPicketInternalId() == null) {
                picket.setPicketInternalId("PIC-" + (highestMeasurementInternalId.incrementAndGet()));
            }
        });
    }

    public void savePickets(final List<Picket> pickets) {
        picketRepository.saveAll(pickets);
    }
}
