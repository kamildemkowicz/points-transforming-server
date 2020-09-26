package points.transforming.app.server.services.history;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.history.*;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.picket.PicketReadModel;
import points.transforming.app.server.repositories.MeasurementRepository;

@Service
@AllArgsConstructor
public class HistoryService {
    private final MeasurementRepository measurementRepository;

    public HistoryChanges getHistoryChanges(final String measurementInternalId) {
        final List<Measurement> measurementChanges = measurementRepository
            .findByMeasurementInternalId(measurementInternalId)
            .orElseThrow(() -> new MeasurementNotFoundException(measurementInternalId));

        if (measurementChanges.size() <= 1)
            return HistoryChanges.builder().changes(new ArrayList<>()).build();

        return HistoryChanges
            .builder()
            .changes(calculateHistory(measurementChanges))
            .build();
    }

    private List<HistoryChange> calculateHistory(final List<Measurement> measurementChanges) {
        final var allHistoryChanges = new ArrayList<HistoryChange>();
        final var i = new AtomicInteger(0);
        final var j = new AtomicInteger(1);

        while(j.get() != measurementChanges.size()) {
            this.createSimpleChanges(measurementChanges.get(i.getAndIncrement()), measurementChanges.get(j.getAndIncrement()))
                .ifPresent(allHistoryChanges::add);
        }

        return allHistoryChanges.stream()
            .sorted(Comparator.comparing(HistoryChange::getDateTime).reversed())
            .collect(Collectors.toUnmodifiableList());
    }

    private Optional<HistoryChange> createSimpleChanges(final Measurement oldMeasurement, final Measurement newMeasurement) {
        final List<HistorySimpleChange> simpleMeasurementChanges = this.compareMeasurementVersions(oldMeasurement, newMeasurement);
        final List<HistoryPicketChange> simplePicketsChanges = this.comparePickets(oldMeasurement.getPickets(), newMeasurement.getPickets());

        if (!simpleMeasurementChanges.isEmpty() || !simplePicketsChanges.isEmpty())
            return Optional.of(HistoryChange
                .builder()
                .measurementChanges(simpleMeasurementChanges)
                .picketChanges(simplePicketsChanges)
                .dateTime(newMeasurement.getCreationDate())
                .build());

        return Optional.empty();
    }

    private List<HistorySimpleChange> compareMeasurementVersions(final Measurement oldMeasurement, final Measurement newMeasurement) {
        final var historyChanges = new ArrayList<HistorySimpleChange>();
        if(!(oldMeasurement.getName().equals(newMeasurement.getName())))
            historyChanges.add(this.createHistoryChange("name", oldMeasurement.getName(), newMeasurement.getName(), HistoryChangeType.CUSTOM));
        if(!oldMeasurement.getOwner().equals(newMeasurement.getOwner()))
            historyChanges.add(this.createHistoryChange("owner", oldMeasurement.getOwner(), newMeasurement.getOwner(), HistoryChangeType.CUSTOM));
        if(!oldMeasurement.getPlace().equals(newMeasurement.getPlace()))
            historyChanges.add(this.createHistoryChange("place", oldMeasurement.getPlace(), newMeasurement.getPlace(), HistoryChangeType.CUSTOM));

        return historyChanges;
    }

    private HistorySimpleChange createHistoryChange(final String label, final String oldValue, final String newValue,
                                                    final HistoryChangeType type) {
            return HistorySimpleChange
                .builder()
                .label(label)
                .oldValue(oldValue)
                .newValue(newValue)
                .type(type)
                .build();
    }

    private List<HistoryPicketChange> comparePickets(final List<Picket> oldMeasurementPickets, final List<Picket> newMeasurementPickets) {
        final var picketsChanges = new ArrayList<HistoryPicketChange>();

        extractDifferentPickets(oldMeasurementPickets, newMeasurementPickets).forEach(addedPicket ->
            picketsChanges.add(
                HistoryPicketChange.builder()
                    .picket(new PicketReadModel(addedPicket))
                    .picketSimpleChanges(List.of(
                        createHistoryChange("Internal ID", null, addedPicket.getPicketInternalId(), HistoryChangeType.ADD),
                        createHistoryChange("name", null, addedPicket.getName(), HistoryChangeType.ADD),
                        createHistoryChange("coordinate X", null, String.valueOf(
                            BigDecimal.valueOf(addedPicket.getCoordinateX()).setScale(2, RoundingMode.CEILING)), HistoryChangeType.ADD),
                        createHistoryChange("coordinate Y", null, String.valueOf(
                            BigDecimal.valueOf(addedPicket.getCoordinateY()).setScale(2, RoundingMode.CEILING)), HistoryChangeType.ADD)
                    ))
                    .type(HistoryChangeType.ADD)
                    .build()
            )
        );

        extractDifferentPickets(newMeasurementPickets, oldMeasurementPickets).forEach(removedPicket ->
            picketsChanges.add(
                HistoryPicketChange.builder()
                    .picket(new PicketReadModel(removedPicket))
                    .picketSimpleChanges(List.of(
                        createHistoryChange("Internal ID", removedPicket.getPicketInternalId(), null, HistoryChangeType.REMOVE),
                        createHistoryChange("name", removedPicket.getName(), null, HistoryChangeType.REMOVE),
                        createHistoryChange("coordinate X", String.valueOf(
                            BigDecimal.valueOf(removedPicket.getCoordinateX()).setScale(2, RoundingMode.CEILING)),
                            null,
                            HistoryChangeType.REMOVE),
                        createHistoryChange("coordinate Y", String.valueOf(
                            BigDecimal.valueOf(removedPicket.getCoordinateY()).setScale(2, RoundingMode.CEILING)),
                            null,
                            HistoryChangeType.REMOVE)
                    ))
                    .type(HistoryChangeType.REMOVE)
                    .build()
            )
        );

        findSamePickets(oldMeasurementPickets, newMeasurementPickets).forEach(changedPicketPair -> {
            final var oldPicket = changedPicketPair.getFirst();
            final var newPicket = changedPicketPair.getSecond();

            createPicketHistoryChange(oldPicket, newPicket).ifPresent(picketsChanges::add);
        });

        return picketsChanges;
    }

    private List<Picket> extractDifferentPickets(final List<Picket> picketsListLeft, final List<Picket> picketsListRight) {
        final var leftInternalPicketsIds = getInternalPicketIds(picketsListLeft);

        return picketsListRight.stream()
            .filter(p -> !leftInternalPicketsIds.contains(p.getPicketInternalId()))
            .collect(Collectors.toUnmodifiableList());
    }

    private Set<String> getInternalPicketIds(final List<Picket> pickets) {
        return pickets.stream()
            .map(Picket::getPicketInternalId)
            .collect(Collectors.toUnmodifiableSet());
    }

    private List<Pair<Picket, Picket>> findSamePickets(final List<Picket> picketsListLeft, final List<Picket> picketsListRight) {
        final var leftInternalPicketIdToPicket = new HashMap<String, Picket>();
        final var rightInternalPicketIdToPicket = new HashMap<String, Picket>();

        picketsListLeft.forEach(picket -> leftInternalPicketIdToPicket.put(picket.getPicketInternalId(), picket));
        picketsListRight.forEach(picket -> rightInternalPicketIdToPicket.put(picket.getPicketInternalId(), picket));

        final var changedPickets = new ArrayList<Pair<Picket, Picket>>();

        leftInternalPicketIdToPicket.forEach((internalPicketId, picket) -> {
            if (rightInternalPicketIdToPicket.containsKey(internalPicketId)) {
                changedPickets.add(Pair.of(picket, rightInternalPicketIdToPicket.get(internalPicketId)));
            }
        });

        return changedPickets;
    }

    private Optional<HistoryPicketChange> createPicketHistoryChange(final Picket oldPicket, final Picket newPicket) {
        final var singlePicketHistoryChanges = new ArrayList<HistorySimpleChange>();
        if (!(oldPicket.getName().equals(newPicket.getName())))
            singlePicketHistoryChanges.add(createHistoryChange("name", oldPicket.getName(), newPicket.getName(), HistoryChangeType.CHANGED_VALUE));
        if (oldPicket.getCoordinateX() != newPicket.getCoordinateX()) {
            singlePicketHistoryChanges.add(createHistoryChange("coordinate X",
                String.valueOf(BigDecimal.valueOf(oldPicket.getCoordinateX()).setScale(2, RoundingMode.CEILING)),
                String.valueOf(BigDecimal.valueOf(newPicket.getCoordinateX()).setScale(2, RoundingMode.CEILING)),
                HistoryChangeType.CHANGED_VALUE));
        }
        if (oldPicket.getCoordinateY() != newPicket.getCoordinateY()) {
            singlePicketHistoryChanges.add(createHistoryChange("coordinate Y",
                String.valueOf(BigDecimal.valueOf(oldPicket.getCoordinateY()).setScale(2, RoundingMode.CEILING)),
                String.valueOf(BigDecimal.valueOf(newPicket.getCoordinateY()).setScale(2, RoundingMode.CEILING)),
                HistoryChangeType.CHANGED_VALUE));
        }

        if (singlePicketHistoryChanges.isEmpty())
            return Optional.empty();

        return Optional.of(HistoryPicketChange.builder()
            .picket(new PicketReadModel(oldPicket))
            .picketSimpleChanges(singlePicketHistoryChanges)
            .type(HistoryChangeType.CHANGED_VALUE)
            .build());
    }
}
