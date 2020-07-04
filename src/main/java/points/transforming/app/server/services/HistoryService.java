package points.transforming.app.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import points.transforming.app.server.models.history.HistoryChange;
import points.transforming.app.server.models.history.HistoryChangeType;
import points.transforming.app.server.models.history.HistoryChanges;
import points.transforming.app.server.models.history.HistorySimpleChanges;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.repositories.MeasurementRepository;

@Service
@AllArgsConstructor
// TODO this service needs to be refactor in next semester
public class HistoryService {
    private final MeasurementRepository measurementRepository;

    public HistoryChanges getHistoryChanges(final String measurementInternalId) {
        // TODO add exception if measurement does not exist
        final List<Measurement> measurementChanges = measurementRepository
            .findByMeasurementInternalId(measurementInternalId)
            .orElse(List.of());

        if (!(measurementChanges.size() > Integer.valueOf(1)))
            return HistoryChanges.builder().changes(new ArrayList<>()).build();

        return HistoryChanges
            .builder()
            .changes(calculateHistory(measurementChanges))
            .build();
    }

    private List<HistorySimpleChanges> calculateHistory(final List<Measurement> measurementChanges) {
        final var allHistoryChanges = new ArrayList<HistorySimpleChanges>();
        final var i = new AtomicInteger(0);
        final var j = new AtomicInteger(1);

        while(j.get() != measurementChanges.size()) {
            final HistorySimpleChanges historySimpleChanges = this.createSimpleChanges(measurementChanges.get(i.getAndIncrement()),
                measurementChanges.get(j.getAndIncrement()));

            if (historySimpleChanges != null)
                allHistoryChanges.add(historySimpleChanges);
        }

        return allHistoryChanges;
    }

    private HistorySimpleChanges createSimpleChanges(final Measurement oldMeasurement, final Measurement newMeasurement) {
        final List<HistoryChange> simpleChanges = this.compareMeasurementVersions(oldMeasurement, newMeasurement);
        final List<HistoryChange> simplePicketsChanges = this.comparePickets(oldMeasurement.getPickets(), newMeasurement.getPickets());

        if (!simpleChanges.isEmpty() || !simplePicketsChanges.isEmpty())
            return HistorySimpleChanges
                .builder()
                .simpleChanges(simpleChanges)
                .picketChanges(simplePicketsChanges)
                .dateTime(newMeasurement.getCreationDate())
                .build();

        return null;
    }

    private List<HistoryChange> compareMeasurementVersions(final Measurement oldMeasurement, final Measurement newMeasurement) {
        final var historyChanges = new ArrayList<HistoryChange>();
        if(!oldMeasurement.getName().equals(newMeasurement.getName()))
            historyChanges.add(this.createHistoryChange("name", oldMeasurement.getName(), newMeasurement.getName(), HistoryChangeType.CUSTOM));
        if(!oldMeasurement.getOwner().equals(newMeasurement.getOwner()))
            historyChanges.add(this.createHistoryChange("owner", oldMeasurement.getOwner(), newMeasurement.getOwner(), HistoryChangeType.CUSTOM));
        if(!oldMeasurement.getPlace().equals(newMeasurement.getPlace()))
            historyChanges.add(this.createHistoryChange("place", oldMeasurement.getPlace(), newMeasurement.getPlace(), HistoryChangeType.CUSTOM));

        return historyChanges;
    }

    private HistoryChange createHistoryChange(final String label, final String oldValue, final String newValue,
                                              final HistoryChangeType type) {
            return HistoryChange
                .builder()
                .label(label)
                .oldValue(oldValue)
                .newValue(newValue)
                .type(type)
                .build();
    }

    private List<HistoryChange> comparePickets(final List<Picket> firstMeasurementsPickets, final List<Picket> secondMeasurementsPickets) {
        final var picketsChanges = new ArrayList<HistoryChange>();

        extractPickets(firstMeasurementsPickets, secondMeasurementsPickets).forEach(addedPicket ->
            picketsChanges.addAll(List.of(
                createHistoryChange("Internal ID", null, addedPicket.getPicketInternalId(), HistoryChangeType.ADD),
                createHistoryChange("name", null, addedPicket.getName(), HistoryChangeType.ADD),
                createHistoryChange("coordinate X", null, String.valueOf(addedPicket.getCoordinateX()), HistoryChangeType.ADD),
                createHistoryChange("coordinate Y", null, String.valueOf(addedPicket.getCoordinateY()), HistoryChangeType.ADD)
            ))
        );

        extractPickets(secondMeasurementsPickets, firstMeasurementsPickets).forEach(addedPicket ->
            picketsChanges.addAll(List.of(
                createHistoryChange("Internal ID", addedPicket.getPicketInternalId(), null, HistoryChangeType.REMOVE),
                createHistoryChange("name", addedPicket.getName(), null, HistoryChangeType.REMOVE),
                createHistoryChange("coordinate X", String.valueOf(addedPicket.getCoordinateX()), null, HistoryChangeType.REMOVE),
                createHistoryChange("coordinate Y", String.valueOf(addedPicket.getCoordinateY()), null, HistoryChangeType.REMOVE)
            ))
        );


        return picketsChanges;
    }
    // TODO nie dziala
    private List<Picket> extractPickets(final List<Picket> oldPickets, final List<Picket> newPickets) {
        return newPickets
            .stream()
            .filter(p -> oldPickets.contains(p))
            .collect(Collectors.toUnmodifiableList());
    }
}
