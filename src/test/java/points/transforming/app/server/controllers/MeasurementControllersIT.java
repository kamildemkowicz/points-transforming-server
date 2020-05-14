package points.transforming.app.server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.exceptions.report.ViolationReport;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.MeasurementReadModel;
import points.transforming.app.server.models.measurement.MeasurementWriteModel;
import points.transforming.app.server.models.picket.PicketWriteModel;
import points.transforming.app.server.repositories.MeasurementRepository;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasurementControllersIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Test
    public void httpGet_returnsAllMeasurements() {
        // given
        var measurementNumber = this.measurementRepository.findAll().size();

        // when
        ResponseEntity<MeasurementReadModel[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements", MeasurementReadModel[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(measurementNumber);
        assertThat(result.getBody()[0].getPickets()).isNotNull();
    }

    @Test
    public void httpGet_returnsAllMeasurements_WithGivenSize() {
        // given

        // when
        ResponseEntity<MeasurementReadModel[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements?size=10" , MeasurementReadModel[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(10);
    }

    @Test
    public void httpGet_returnsAllMeasurements_WithGivenSizeAndPage() {
        // given

        // when
        ResponseEntity<MeasurementReadModel[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements?size=10&page=2" , MeasurementReadModel[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(10);
        assertThat(result.getBody()[0].getId()).isGreaterThan(20);
    }

    @Test
    public void httpGet_returnsAllMeasurements_WithGivenSizeAndPageAndSortByPlace() {
        // given

        // when
        ResponseEntity<MeasurementReadModel[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements?size=10&page=1&sort=place,asc" , MeasurementReadModel[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(10);
        assertThat(result.getBody()[0].getPlace()).isEqualTo("Bydgoszcz");
    }

    @Test
    public void httpPost_createMeasurement_emptySetOfPickets() {
        // given
        var measurementWriteModel = new MeasurementWriteModel();
        measurementWriteModel.setName("testPlace");
        measurementWriteModel.setPlace("Gdansk");

        // when
        ResponseEntity<MeasurementReadModel> result= this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void httpPost_createMeasurementWithoutPlace_throwException() {
        // given
        var measurementWriteModel = new MeasurementWriteModel();
        measurementWriteModel.setName("testPlace");

        // when
        ResponseEntity<ViolationReport[]> result= this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        ViolationReport[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(result.getBody()).length).isEqualTo(1);
        assertThat(Objects.requireNonNull(result.getBody()[0].getCause())).isEqualTo("place");
        assertThat(Objects.requireNonNull(result.getBody()[0].getMessage())).isEqualTo("must not be null");
        assertThat(Objects.requireNonNull(result.getBody()[0].getField())).isEqualTo("null");
    }

    @Test
    public void httpPost_createMeasurementWithoutPlaceAndName_throwExceptions() {
        // given
        var measurementWriteModel = new MeasurementWriteModel();

        // when
        ResponseEntity<ViolationReport[]> result= this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        ViolationReport[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(result.getBody()).length).isEqualTo(2);
    }

    @Test
    public void httpPost_createMeasurementWithPickets() {
        // given
        var picket1 = new PicketWriteModel();
        picket1.setPicketId("picket1");
        picket1.setCoordinateX(40.30);
        picket1.setCoordinateY(42.30);

        var picket2 = new PicketWriteModel();
        picket2.setPicketId("picket2");
        picket2.setCoordinateX(30.30);
        picket2.setCoordinateY(32.30);

        var measurementWriteModel = new MeasurementWriteModel();
        measurementWriteModel.setName("testPlace2");
        measurementWriteModel.setPlace("Gdansk");
        measurementWriteModel.setPickets(List.of(picket1, picket2));

        // when
        ResponseEntity<MeasurementReadModel> result= this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
