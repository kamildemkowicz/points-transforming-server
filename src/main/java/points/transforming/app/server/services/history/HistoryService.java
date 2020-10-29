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
import points.transforming.app.server.models.picket.PicketResponse;
import points.transforming.app.server.repositories.MeasurementRepository;

@Service
@AllArgsConstructor
public class HistoryService {
    private final MeasurementRepository measurementRepository;

    public HistoryChanges getHistoryChanges(final String measurementInternalId) {
        final List<Measurement> measurementChanges = measurementRepository
            .findByMeasurementInternalId(measurementInternalId)
            .orElseThrow(() -> new MeasurementNotFoundException(Error.MEASUREMENT_DOES_NOT_EXIST_PTS100));

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
            historyChanges.add(this.createHistoryChange("Name", oldMeasurement.getName(), newMeasurement.getName(), HistoryChangeType.CUSTOM));
        if(!oldMeasurement.getOwner().equals(newMeasurement.getOwner()))
            historyChanges.add(this.createHistoryChange("Owner", oldMeasurement.getOwner(), newMeasurement.getOwner(), HistoryChangeType.CUSTOM));
        if(!oldMeasurement.getPlace().equals(newMeasurement.getPlace()))
            historyChanges.add(this.createHistoryChange("Place", oldMeasurement.getPlace(), newMeasurement.getPlace(), HistoryChangeType.CUSTOM));

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

        extractDifferentPickets(oldMeasurementPickets, newMeasurementPickets).forEach(addedPicket -> {
            final var picketSimpleChanges = new ArrayList<>(List.of(
                createHistoryChange("Internal ID", null, addedPicket.getPicketInternalId(), HistoryChangeType.ADD),
                createHistoryChange("Name", null, addedPicket.getName(), HistoryChangeType.ADD))
            );

            if (Objects.nonNull(addedPicket.getLongitude())) {
                picketSimpleChanges.add(createHistoryChange("Longitude", null,
                    String.valueOf(BigDecimal.valueOf(addedPicket.getLongitude()).setScale(2, RoundingMode.CEILING)), HistoryChangeType.ADD));
            }

            if (Objects.nonNull(addedPicket.getLatitude())) {
                picketSimpleChanges.add(createHistoryChange("Latitude", null, String.valueOf(
                    BigDecimal.valueOf(addedPicket.getLatitude()).setScale(2, RoundingMode.CEILING)), HistoryChangeType.ADD));
            }

            if (Objects.nonNull(addedPicket.getCoordinateX2000())) {
                picketSimpleChanges.add(createHistoryChange("Coordinate X 2000", null, String.valueOf(
                    BigDecimal.valueOf(addedPicket.getCoordinateX2000()).setScale(2, RoundingMode.CEILING)), HistoryChangeType.ADD));
            }

            if (Objects.nonNull(addedPicket.getCoordinateY2000())) {
                picketSimpleChanges.add(createHistoryChange("Coordinate Y 2000", null, String.valueOf(
                    BigDecimal.valueOf(addedPicket.getCoordinateY2000()).setScale(2, RoundingMode.CEILING)), HistoryChangeType.ADD));
            }

            final var historyPicketChange = HistoryPicketChange.builder()
                .picket(PicketResponse.of(addedPicket))
                .picketSimpleChanges(picketSimpleChanges)
                .type(HistoryChangeType.ADD)
                .build();
            picketsChanges.add(historyPicketChange);
        });

        extractDifferentPickets(newMeasurementPickets, oldMeasurementPickets).forEach(removedPicket -> {
            final var picketSimpleChanges = new ArrayList<>(List.of(
                createHistoryChange("Internal ID", removedPicket.getPicketInternalId(), null, HistoryChangeType.REMOVE),
                createHistoryChange("Name", removedPicket.getName(), null, HistoryChangeType.REMOVE))
            );

            if (Objects.nonNull(removedPicket.getLongitude())) {
                picketSimpleChanges.add(createHistoryChange("Longitude", String.valueOf(
                    BigDecimal.valueOf(removedPicket.getLongitude()).setScale(2, RoundingMode.CEILING)),
                    null,
                    HistoryChangeType.REMOVE));
            }

            if (Objects.nonNull(removedPicket.getLatitude())) {
                picketSimpleChanges.add(createHistoryChange("Latitude", String.valueOf(
                    BigDecimal.valueOf(removedPicket.getLatitude()).setScale(2, RoundingMode.CEILING)),
                    null,
                    HistoryChangeType.REMOVE));
            }

            if (Objects.nonNull(removedPicket.getCoordinateX2000())) {
                picketSimpleChanges.add(createHistoryChange("Coordinate X 2000", String.valueOf(
                    BigDecimal.valueOf(removedPicket.getCoordinateX2000()).setScale(2, RoundingMode.CEILING)),
                    null,
                    HistoryChangeType.REMOVE));
            }

            if (Objects.nonNull(removedPicket.getCoordinateY2000())) {
                picketSimpleChanges.add(createHistoryChange("Coordinate Y 2000", String.valueOf(
                    BigDecimal.valueOf(removedPicket.getCoordinateY2000()).setScale(2, RoundingMode.CEILING)),
                    null,
                    HistoryChangeType.REMOVE));
            }

            final var historyPicketChange = HistoryPicketChange.builder()
                .picket(PicketResponse.of(removedPicket))
                .picketSimpleChanges(picketSimpleChanges)
                .type(HistoryChangeType.REMOVE)
                .build();
            picketsChanges.add(historyPicketChange);
        });

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
            singlePicketHistoryChanges.add(createHistoryChange("Name", oldPicket.getName(), newPicket.getName(), HistoryChangeType.CHANGED_VALUE));

        createHistorySimpleChange("Longitude", oldPicket.getLongitude(), newPicket.getLongitude()).ifPresent(singlePicketHistoryChanges::add);
        createHistorySimpleChange("Latitude", oldPicket.getLatitude(), newPicket.getLatitude()).ifPresent(singlePicketHistoryChanges::add);
        createHistorySimpleChange("Coordinate X 2000", oldPicket.getCoordinateX2000(), newPicket.getCoordinateX2000()).ifPresent(singlePicketHistoryChanges::add);
        createHistorySimpleChange("Coordinate Y 2000", oldPicket.getCoordinateY2000(), newPicket.getCoordinateY2000()).ifPresent(singlePicketHistoryChanges::add);

        if (singlePicketHistoryChanges.isEmpty())
            return Optional.empty();

        return Optional.of(HistoryPicketChange.builder()
            .picket(PicketResponse.of(oldPicket))
            .picketSimpleChanges(singlePicketHistoryChanges)
            .type(HistoryChangeType.CHANGED_VALUE)
            .build());
    }

    private Optional<HistorySimpleChange> createHistorySimpleChange(final String label, final Double oldPicketValue, final Double newPicketValue) {
        if (Objects.nonNull(oldPicketValue) && Objects.nonNull(newPicketValue) && !oldPicketValue.equals(newPicketValue)) {
            return Optional.of(createHistoryChange(label,
                String.valueOf(BigDecimal.valueOf(oldPicketValue).setScale(2, RoundingMode.CEILING)),
                String.valueOf(BigDecimal.valueOf(newPicketValue).setScale(2, RoundingMode.CEILING)),
                HistoryChangeType.CHANGED_VALUE)
            );
        }

        if (Objects.isNull(oldPicketValue) && Objects.nonNull(newPicketValue)) {
            return Optional.of(createHistoryChange(label,
                null,
                String.valueOf(BigDecimal.valueOf(newPicketValue).setScale(2, RoundingMode.CEILING)),
                HistoryChangeType.CHANGED_VALUE)
            );
        }

        if (Objects.nonNull(oldPicketValue) && Objects.isNull(newPicketValue)) {
            return Optional.of(createHistoryChange(label,
                String.valueOf(BigDecimal.valueOf(oldPicketValue).setScale(2, RoundingMode.CEILING)),
                null,
                HistoryChangeType.CHANGED_VALUE)
            );
        }

        return Optional.empty();
    }

    enum Error {
        MEASUREMENT_DOES_NOT_EXIST_PTS100
    }
}
