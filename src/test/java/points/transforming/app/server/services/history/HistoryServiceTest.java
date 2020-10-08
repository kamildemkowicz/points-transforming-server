package points.transforming.app.server.services.history;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.history.HistoryChangeType;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.repositories.MeasurementRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@UnitTestWithMockito
public class HistoryServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @InjectMocks
    private HistoryService historyService;

    private final TestHistoryServiceFactory testHistoryServiceFactory = new TestHistoryServiceFactory();

    @Test
    public void shouldThrowMeasurementNotFoundExceptionWhenGivenMeasurementDoesNotExist() {
        // given
        when(measurementRepository.findByMeasurementInternalId("notExist")).thenReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> historyService.getHistoryChanges("notExist"))
            .isInstanceOf(MeasurementNotFoundException.class)
            .hasFieldOrPropertyWithValue("measurementId", "notExist");
    }

    @Test
    public void shouldGetHistoryChangesReturnsEmptyListWhenMeasurementHasOnlyOneVersion() {
        // given
        final var mes = new Measurement();
        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges()).isEmpty();
    }

    @Test
    public void shouldGetHistoryChangesReturnsEmptyListWhenMeasurementHasTwoVersionAndNoChanges() {
        // given
        final var mes = new Measurement();
        mes.setName("Measurement1");
        mes.setPlace("Gdansk");
        mes.setOwner("Owner1");
        mes.setVersion(1);
        mes.setPickets(List.of());

        final var mes2 = new Measurement();
        mes2.setName("Measurement1");
        mes2.setPlace("Gdansk");
        mes2.setOwner("Owner1");
        mes2.setVersion(2);
        mes2.setPickets(List.of());

        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes, mes2)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges()).isEmpty();
    }

    @Test
    public void shouldGetHistoryChangesReturnsCorrectChangesWhenMeasurementHasCommonChangesMade() {
        // given
        final var now = LocalDateTime.now();
        final var mes = new Measurement();
        mes.setName("Measurement1");
        mes.setPlace("Gdansk");
        mes.setOwner("Owner1");
        mes.setVersion(1);
        mes.setPickets(List.of());

        final var mes2 = new Measurement();
        mes2.setName("Measurement2");
        mes2.setPlace("Zielona Gora");
        mes2.setOwner("Owner1");
        mes2.setCreationDate(now);
        mes2.setVersion(2);
        mes2.setPickets(List.of());

        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes, mes2)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges().size()).isEqualTo(1);
        assertThat(result.getChanges().get(0).getMeasurementChanges().size()).isEqualTo(2);
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getLabel()).isEqualTo("name");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getOldValue()).isEqualTo("Measurement1");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getNewValue()).isEqualTo("Measurement2");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getType()).isEqualTo(HistoryChangeType.CUSTOM);
        assertThat(result.getChanges().get(0).getPicketChanges()).isEmpty();
        assertThat(result.getChanges().get(0).getDateTime()).isEqualTo(now);
    }

    @Test
    public void shouldGetHistoryChangesReturnsCorrectChangesWhenMeasurementHasCommonChangesMadeAndTwoVersions() {
        // given
        final var now = LocalDateTime.now();
        final var now2 = LocalDateTime.now().plusHours(1);
        final var mes = new Measurement();
        mes.setName("Measurement1");
        mes.setPlace("Gdansk");
        mes.setOwner("Owner1");
        mes.setVersion(1);
        mes.setPickets(List.of());

        final var mes2 = new Measurement();
        mes2.setName("Measurement2");
        mes2.setPlace("Zielona Gora");
        mes2.setOwner("Owner2");
        mes2.setCreationDate(now);
        mes2.setVersion(2);
        mes2.setPickets(List.of());

        final var mes3 = new Measurement();
        mes3.setName("Measurement1");
        mes3.setPlace("Gdansk");
        mes3.setOwner("Owner1");
        mes3.setCreationDate(now2);
        mes3.setVersion(2);
        mes3.setPickets(List.of());

        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes, mes2, mes3)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges().size()).isEqualTo(2);

        assertThat(result.getChanges().get(0).getMeasurementChanges().size()).isEqualTo(3);
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getLabel()).isEqualTo("name");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getOldValue()).isEqualTo("Measurement2");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getNewValue()).isEqualTo("Measurement1");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getType()).isEqualTo(HistoryChangeType.CUSTOM);
        assertThat(result.getChanges().get(0).getPicketChanges()).isEmpty();
        assertThat(result.getChanges().get(0).getDateTime()).isEqualTo(now2);

        assertThat(result.getChanges().get(1).getMeasurementChanges().size()).isEqualTo(3);
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getLabel()).isEqualTo("name");
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getOldValue()).isEqualTo("Measurement1");
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getNewValue()).isEqualTo("Measurement2");
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getType()).isEqualTo(HistoryChangeType.CUSTOM);
        assertThat(result.getChanges().get(1).getPicketChanges()).isEmpty();
        assertThat(result.getChanges().get(1).getDateTime()).isEqualTo(now);
    }

    @Test
    public void shouldGetHistoryChangesReturnsCorrectChangesWhenMeasurementHasNewPicketsAdded() {
        // given
        final var now = LocalDateTime.now();
        final var mes = new Measurement();
        mes.setName("Measurement1");
        mes.setPlace("Gdansk");
        mes.setOwner("Owner1");
        mes.setVersion(1);
        mes.setPickets(List.of());

        final var mes2 = new Measurement();
        mes2.setName("Measurement1");
        mes2.setPlace("Gdansk");
        mes2.setOwner("Owner1");
        mes2.setCreationDate(now);
        mes2.setVersion(2);
        mes2.setPickets(
            List.of(
                testHistoryServiceFactory.createValidPicket(1, mes2),
                testHistoryServiceFactory.createValidPicket(2, mes2)
            )
        );

        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes, mes2)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges().size()).isEqualTo(1);
        assertThat(result.getChanges().get(0).getDateTime()).isEqualTo(now);
        assertThat(result.getChanges().get(0).getPicketChanges().size()).isEqualTo(2);
        assertThat(result.getChanges().get(0).getMeasurementChanges().size()).isEqualTo(0);

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getType()).isEqualTo(HistoryChangeType.ADD);

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getNewValue()).isEqualTo("PIC-1");
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getLabel()).isEqualTo("Internal ID");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getNewValue()).isEqualTo("picket1");
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getLabel()).isEqualTo("name");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getNewValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getLabel()).isEqualTo("coordinate X");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getNewValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getLabel()).isEqualTo("coordinate Y");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(0).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(0).getNewValue()).isEqualTo("PIC-2");
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(0).getLabel()).isEqualTo("Internal ID");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(1).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(1).getNewValue()).isEqualTo("picket2");
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(1).getLabel()).isEqualTo("name");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(2).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(2).getNewValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(2).getLabel()).isEqualTo("coordinate X");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(3).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(3).getNewValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(3).getLabel()).isEqualTo("coordinate Y");
    }

    @Test
    public void shouldGetHistoryChangesReturnsCorrectChangesWhenMeasurementHasNewPicketsRemoved() {
        // given
        final var now = LocalDateTime.now();
        final var mes = new Measurement();
        final var pic1 = testHistoryServiceFactory.createValidPicket(1, mes);
        mes.setName("Measurement1");
        mes.setPlace("Gdansk");
        mes.setOwner("Owner1");
        mes.setVersion(1);
        mes.setPickets(
            List.of(
                pic1,
                testHistoryServiceFactory.createValidPicket(2, mes)
            )
        );

        final var mes2 = new Measurement();
        mes2.setName("Measurement1");
        mes2.setPlace("Gdansk");
        mes2.setOwner("Owner1");
        mes2.setCreationDate(now);
        mes2.setVersion(2);
        mes2.setPickets(
            List.of(
                pic1
            )
        );

        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes, mes2)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges().size()).isEqualTo(1);
        assertThat(result.getChanges().get(0).getDateTime()).isEqualTo(now);
        assertThat(result.getChanges().get(0).getPicketChanges().size()).isEqualTo(1);
        assertThat(result.getChanges().get(0).getMeasurementChanges().size()).isEqualTo(0);

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getType()).isEqualTo(HistoryChangeType.REMOVE);

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getOldValue()).isEqualTo("PIC-2");
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getLabel()).isEqualTo("Internal ID");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getOldValue()).isEqualTo("picket2");
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getLabel()).isEqualTo("name");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getOldValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getLabel()).isEqualTo("coordinate X");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getOldValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getLabel()).isEqualTo("coordinate Y");
    }

    @Test
    public void shouldGetHistoryChangesReturnsCorrectChangesWhenMeasurementHasNewPicketsAddedRemovedAndModified() {
        // given
        final var now = LocalDateTime.now();

        final var mes = new Measurement();
        final var picket1Old = testHistoryServiceFactory.createValidPicket(1, mes);
        mes.setName("Measurement1");
        mes.setPlace("Gdansk");
        mes.setOwner("Owner1");
        mes.setVersion(1);
        mes.setPickets(
            List.of(
                picket1Old,
                testHistoryServiceFactory.createValidPicket(2, mes)
            )
        );

        final var mes2 = new Measurement();
        final var picket1New = testHistoryServiceFactory.createValidPicket(1, mes);
        picket1New.setName("NewName");
        mes2.setName("Measurement1");
        mes2.setPlace("Gdansk");
        mes2.setOwner("Owner1");
        mes2.setCreationDate(now);
        mes2.setVersion(2);
        mes2.setPickets(
            List.of(
                picket1New,
                testHistoryServiceFactory.createValidPicket(3, mes2)
            )
        );

        when(measurementRepository.findByMeasurementInternalId("MES-1")).thenReturn(Optional.of(List.of(mes, mes2)));

        // when
        final var result = historyService.getHistoryChanges("MES-1");

        // then
        assertThat(result.getChanges().size()).isEqualTo(1);
        assertThat(result.getChanges().get(0).getDateTime()).isEqualTo(now);
        assertThat(result.getChanges().get(0).getPicketChanges().size()).isEqualTo(3);
        assertThat(result.getChanges().get(0).getMeasurementChanges().size()).isEqualTo(0);

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getType()).isEqualTo(HistoryChangeType.ADD);

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getNewValue()).isEqualTo("PIC-3");
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(0).getLabel()).isEqualTo("Internal ID");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getNewValue()).isEqualTo("picket3");
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(1).getLabel()).isEqualTo("name");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getNewValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(2).getLabel()).isEqualTo("coordinate X");

        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getOldValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getNewValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(0).getPicketSimpleChanges().get(3).getLabel()).isEqualTo("coordinate Y");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getType()).isEqualTo(HistoryChangeType.REMOVE);

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(0).getOldValue()).isEqualTo("PIC-2");
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(0).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(0).getLabel()).isEqualTo("Internal ID");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(1).getOldValue()).isEqualTo("picket2");
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(1).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(1).getLabel()).isEqualTo("name");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(2).getOldValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(2).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(2).getLabel()).isEqualTo("coordinate X");

        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(3).getOldValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(3).getNewValue()).isNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(1).getPicketSimpleChanges().get(3).getLabel()).isEqualTo("coordinate Y");

        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(0).getOldValue()).isNotNull();
        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(0).getNewValue()).isEqualTo("NewName");
        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(0).getLabel()).isEqualTo("name");

        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(1).getOldValue())
            .isEqualTo(String.valueOf(BigDecimal.valueOf(picket1Old.getCoordinateX()).setScale(2, RoundingMode.CEILING)));
        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(1).getNewValue())
            .isEqualTo(String.valueOf(BigDecimal.valueOf(picket1New.getCoordinateX()).setScale(2, RoundingMode.CEILING)));
        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(1).getLabel()).isEqualTo("coordinate X");

        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(2).getOldValue())
            .isEqualTo(String.valueOf(BigDecimal.valueOf(picket1Old.getCoordinateY()).setScale(2, RoundingMode.CEILING)));
        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(2).getNewValue())
            .isEqualTo(String.valueOf(BigDecimal.valueOf(picket1New.getCoordinateY()).setScale(2, RoundingMode.CEILING)));
        assertThat(result.getChanges().get(0).getPicketChanges().get(2).getPicketSimpleChanges().get(2).getLabel()).isEqualTo("coordinate Y");
    }
}
