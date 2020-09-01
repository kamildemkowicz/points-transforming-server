package points.transforming.app.server.services;

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
        mes2.setOwner("Owner1");
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

        assertThat(result.getChanges().get(0).getMeasurementChanges().size()).isEqualTo(2);
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getLabel()).isEqualTo("name");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getOldValue()).isEqualTo("Measurement1");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getNewValue()).isEqualTo("Measurement2");
        assertThat(result.getChanges().get(0).getMeasurementChanges().get(0).getType()).isEqualTo(HistoryChangeType.CUSTOM);
        assertThat(result.getChanges().get(0).getPicketChanges()).isEmpty();
        assertThat(result.getChanges().get(0).getDateTime()).isEqualTo(now);

        assertThat(result.getChanges().get(1).getMeasurementChanges().size()).isEqualTo(2);
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getLabel()).isEqualTo("name");
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getOldValue()).isEqualTo("Measurement2");
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getNewValue()).isEqualTo("Measurement1");
        assertThat(result.getChanges().get(1).getMeasurementChanges().get(0).getType()).isEqualTo(HistoryChangeType.CUSTOM);
        assertThat(result.getChanges().get(1).getPicketChanges()).isEmpty();
        assertThat(result.getChanges().get(1).getDateTime()).isEqualTo(now2);
    }


}