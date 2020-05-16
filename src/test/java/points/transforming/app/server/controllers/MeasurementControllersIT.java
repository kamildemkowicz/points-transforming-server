package points.transforming.app.server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import points.transforming.app.server.exceptions.report.ViolationReport;
import points.transforming.app.server.models.measurement.MeasurementReadModel;
import points.transforming.app.server.models.measurement.MeasurementWriteModel;
import points.transforming.app.server.models.picket.PicketWriteModel;
import points.transforming.app.server.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.*;

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
        measurementWriteModel.setName(UUID.randomUUID().toString());
        measurementWriteModel.setPlace("Gdansk");
        measurementWriteModel.setOwner("Kamil Demkowicz");

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
    public void httpPost_createMeasurementBadRequest_throwException() {
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
        assertThat(Objects.requireNonNull(result.getBody()).length).isEqualTo(2);
        assertThat(result.getBody()[0].getMessage()).isEqualTo("must not be null");
    }

    @Test
    public void httpPost_createMeasurementWithoutPlaceAndNameAndOwner_throwExceptions() {
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
        assertThat(Objects.requireNonNull(result.getBody()).length).isEqualTo(3);
    }

    @Test
    public void httpPost_createMeasurementWithPickets() {
        // given
        var measurementName = UUID.randomUUID().toString();
        var time = LocalDateTime.now();
        var picket1 = new PicketWriteModel();
        picket1.setName("picket1");
        picket1.setCoordinateX(40.30);
        picket1.setCoordinateY(42.30);

        var picket2 = new PicketWriteModel();
        picket2.setName("picket2");
        picket2.setCoordinateX(30.30);
        picket2.setCoordinateY(32.30);

        var measurementWriteModel = new MeasurementWriteModel();
        measurementWriteModel.setName(measurementName);
        measurementWriteModel.setPlace("Gdansk");
        measurementWriteModel.setOwner("Kamil Demkowicz");
        measurementWriteModel.setPickets(List.of(picket1, picket2));

        // when
        ResponseEntity<MeasurementReadModel> result= this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(result.getBody()).getPickets().size()).isEqualTo(2);
        assertThat(result.getBody().getPlace()).isEqualTo("Gdansk");
        assertThat(result.getBody().getName()).isEqualTo(measurementName);
        assertThat(result.getBody().getOwner()).isEqualTo("Kamil Demkowicz");
        assertThat(result.getBody().getCreationDate()).isNotNull().isAfter(time);
    }

    @Test
    public void httpPost_createMeasurementWithTheSameNameButDifferentInternalIds() {
        // given
        var measurementName = UUID.randomUUID().toString();
        var measurementWriteModel = new MeasurementWriteModel();
        measurementWriteModel.setName(measurementName);
        measurementWriteModel.setPlace("Gdansk");
        measurementWriteModel.setOwner("Kamil Demkowicz");

        // when
        ResponseEntity<MeasurementReadModel> result= this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        MeasurementReadModel.class);
        String measurementInternalId1 = this.measurementRepository
                .findById(Objects.requireNonNull(result.getBody()).getId())
                .get()
                .getMeasurementInternalId();

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // when
        ResponseEntity<MeasurementReadModel> result2 = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        MeasurementReadModel.class);

        String measurementInternalId2 = this.measurementRepository
                .findById(Objects.requireNonNull(result2.getBody()).getId())
                .get()
                .getMeasurementInternalId();

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Integer.parseInt(measurementInternalId1.replace("MES-", "")))
                .isLessThan(Integer.parseInt(measurementInternalId2.replace("MES-", "")));
    }

    @Test
    public void httpPost_createMeasurementWithDuplicatedPickets_throwDataIntegrityViolationException() {
        // given
        var measurementName = UUID.randomUUID().toString();
        var picket1 = new PicketWriteModel();
        picket1.setName("picket1");
        picket1.setCoordinateX(40.30);
        picket1.setCoordinateY(42.30);

        var picket2 = new PicketWriteModel();
        picket2.setName("picket1");
        picket2.setCoordinateX(30.30);
        picket2.setCoordinateY(32.30);

        var measurementWriteModel = new MeasurementWriteModel();
        measurementWriteModel.setName(measurementName);
        measurementWriteModel.setPlace("Gdansk");
        measurementWriteModel.setOwner("Kamil Demkowicz");
        measurementWriteModel.setPickets(List.of(picket1, picket2));

        // when
        ResponseEntity<ViolationReport> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        measurementWriteModel,
                        ViolationReport.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(result.getBody()).getMessage()).isEqualTo("The measurement with given payload already exists!");
        assertThat(Objects.requireNonNull(result.getBody()).getCause()).containsIgnoringCase("pk_name_measurement_id");
    }

    @Test
    public void httpPut_createTwoMeasurementVersions_WithoutChanges() {
        // given
        var measurementName = UUID.randomUUID().toString();

        var picket1 = new PicketWriteModel();
        picket1.setName("picket1");
        picket1.setCoordinateX(40.30);
        picket1.setCoordinateY(42.30);

        var picket2 = new PicketWriteModel();
        picket2.setName("picket2");
        picket2.setCoordinateX(40.30);
        picket2.setCoordinateY(42.30);

        // when
        ResponseEntity<MeasurementReadModel> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of(picket1, picket2)),
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        var picket1_2 = new PicketWriteModel();
        picket1_2.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId());
        picket1_2.setName("picket1");
        picket1_2.setCoordinateX(40.30);
        picket1_2.setCoordinateY(42.30);

        var picket2_2 = new PicketWriteModel();
        picket2_2.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(1).getPicketInternalId());
        picket2_2.setName("picket2");
        picket2_2.setCoordinateX(40.30);
        picket2_2.setCoordinateY(42.30);

        // when
        ResponseEntity<MeasurementReadModel> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getId(),
                        this.createNewMeasurementRequestBody(measurementName, List.of(picket1_2, picket2_2)),
                        MeasurementReadModel.class
        );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getName()).isEqualTo(Objects.requireNonNull(result2.getBody()).getName());
        assertThat(result.getBody().getPlace()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPlace());
        assertThat(result.getBody().getCreationDate()).isBefore(Objects.requireNonNull(result2.getBody()).getCreationDate());
        assertThat(result.getBody().getId()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getId());

        assertThat(result.getBody().getPickets().get(0).getId()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getId());
        assertThat(result.getBody().getPickets().get(1).getId()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(1).getId());
        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getPicketInternalId());
        assertThat(result.getBody().getPickets().get(1).getPicketInternalId()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(1).getPicketInternalId());
        assertThat(result.getBody().getPickets().get(0).getName()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getName());
        assertThat(result.getBody().getPickets().get(0).getCoordinateX()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getCoordinateX());
        assertThat(result.getBody().getPickets().get(0).getCoordinateY()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getCoordinateY());
    }

    @Test
    public void httpPut_updateMeasurement_ChangeMetaProperties() {
        // given
        var measurementName = UUID.randomUUID().toString();

        // when
        ResponseEntity<MeasurementReadModel> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of()),
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        var measurementRequestBody = new MeasurementWriteModel();
        measurementRequestBody.setOwner("Mariusz Kaczmarek");
        measurementRequestBody.setName("nowaNazwa");
        measurementRequestBody.setPlace("Zielona Góra");

        // when
        ResponseEntity<MeasurementReadModel> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getId(),
                        measurementRequestBody,
                        MeasurementReadModel.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getName()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getName());
        assertThat(Objects.requireNonNull(result2.getBody()).getName()).isEqualTo("nowaNazwa");

        assertThat(result.getBody().getPlace()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getPlace());
        assertThat(Objects.requireNonNull(result2.getBody()).getPlace()).isEqualTo("Zielona Góra");

        assertThat(result.getBody().getOwner()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getOwner());
        assertThat(Objects.requireNonNull(result2.getBody()).getOwner()).isEqualTo("Mariusz Kaczmarek");

        assertThat(result.getBody().getCreationDate()).isBefore(Objects.requireNonNull(result2.getBody()).getCreationDate());
        assertThat(result.getBody().getId()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getId());
    }

    @Test
    public void httpPut_updateMeasurement_add_one_picket() {
        // given
        var measurementName = UUID.randomUUID().toString();

        // when
        ResponseEntity<MeasurementReadModel> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of()),
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        var picketWriteModel = new PicketWriteModel();
        picketWriteModel.setCoordinateX(30);
        picketWriteModel.setCoordinateY(40);
        picketWriteModel.setName("NowaPikieta");

        // when
        ResponseEntity<MeasurementReadModel> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getId(),
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel)),
                        MeasurementReadModel.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getName()).isEqualTo(Objects.requireNonNull(result2.getBody()).getName());
        assertThat(result.getBody().getPlace()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPlace());
        assertThat(result.getBody().getCreationDate()).isBefore(Objects.requireNonNull(result2.getBody()).getCreationDate());
        assertThat(result.getBody().getId()).isNotEqualTo(Objects.requireNonNull(result2.getBody()).getId());

        assertThat(result.getBody().getPickets().size()).isEqualTo(0);
        assertThat(result2.getBody().getPickets().size()).isEqualTo(1);
    }

    @Test
    public void httpPut_updateMeasurement_addOnePicket_and_modifyExistOne() {
        // given
        var measurementName = UUID.randomUUID().toString();

        var picketWriteModel1 = new PicketWriteModel();
        picketWriteModel1.setCoordinateX(1);
        picketWriteModel1.setCoordinateY(2);
        picketWriteModel1.setName("Picket1");

        var picketWriteModel2 = new PicketWriteModel();
        picketWriteModel2.setCoordinateX(3);
        picketWriteModel2.setCoordinateY(4);
        picketWriteModel2.setName("Picket2");

        // when
        ResponseEntity<MeasurementReadModel> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel1, picketWriteModel2)),
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        picketWriteModel1.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId());
        picketWriteModel1.setCoordinateX(2);
        picketWriteModel1.setCoordinateY(10);
        picketWriteModel1.setName("Picket10");

        picketWriteModel2.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(1).getPicketInternalId());

        var picketWriteModel = new PicketWriteModel();
        picketWriteModel.setCoordinateX(30);
        picketWriteModel.setCoordinateY(40);
        picketWriteModel.setName("NowaPikieta");

        // when
        ResponseEntity<MeasurementReadModel> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getId(),
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel1, picketWriteModel2, picketWriteModel)),
                        MeasurementReadModel.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody().getPickets().size()).isEqualTo(2);
        assertThat(Objects.requireNonNull(result2.getBody()).getPickets().size()).isEqualTo(3);

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isEqualTo(result2.getBody().getPickets().get(0).getPicketInternalId());
        assertThat(result.getBody().getPickets().get(0).getId()).isNotEqualTo(result2.getBody().getPickets().get(0).getId());
        assertThat(result.getBody().getPickets().get(0).getName()).isNotEqualTo(result2.getBody().getPickets().get(0).getName());
        assertThat(result.getBody().getPickets().get(0).getCoordinateX()).isNotEqualTo(result2.getBody().getPickets().get(0).getCoordinateX());
        assertThat(result.getBody().getPickets().get(0).getCoordinateY()).isNotEqualTo(result2.getBody().getPickets().get(0).getCoordinateY());

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isNotEqualTo(result2.getBody().getPickets().get(2).getPicketInternalId());
        assertThat(result2.getBody().getPickets().get(2).getName()).isEqualTo("NowaPikieta");
        assertThat(result2.getBody().getPickets().get(2).getCoordinateY()).isEqualTo(40);
        assertThat(result2.getBody().getPickets().get(2).getCoordinateX()).isEqualTo(30);
    }

    @Test
    public void httpPut_updateMeasurement_addOnePicket_and_removeExistOne() {
        // given
        var measurementName = UUID.randomUUID().toString();

        var picketWriteModel1 = new PicketWriteModel();
        picketWriteModel1.setCoordinateX(1);
        picketWriteModel1.setCoordinateY(2);
        picketWriteModel1.setName("Picket1");

        var picketWriteModel2 = new PicketWriteModel();
        picketWriteModel2.setCoordinateX(3);
        picketWriteModel2.setCoordinateY(4);
        picketWriteModel2.setName("Picket2");

        // when
        ResponseEntity<MeasurementReadModel> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel1, picketWriteModel2)),
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        picketWriteModel1.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId());
        picketWriteModel1.setCoordinateX(2);
        picketWriteModel1.setCoordinateY(10);
        picketWriteModel1.setName("Picket10");

        picketWriteModel2.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(1).getPicketInternalId());

        var picketWriteModel = new PicketWriteModel();
        picketWriteModel.setCoordinateX(30);
        picketWriteModel.setCoordinateY(40);
        picketWriteModel.setName("Picket2");

        // when
        ResponseEntity<MeasurementReadModel> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getId(),
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel1, picketWriteModel)),
                        MeasurementReadModel.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody().getPickets().size()).isEqualTo(2);
        assertThat(Objects.requireNonNull(result2.getBody()).getPickets().size()).isEqualTo(2);

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isEqualTo(result2.getBody().getPickets().get(0).getPicketInternalId());
        assertThat(result.getBody().getPickets().get(0).getId()).isNotEqualTo(result2.getBody().getPickets().get(0).getId());
        assertThat(result.getBody().getPickets().get(0).getName()).isNotEqualTo(result2.getBody().getPickets().get(0).getName());
        assertThat(result.getBody().getPickets().get(0).getCoordinateX()).isNotEqualTo(result2.getBody().getPickets().get(0).getCoordinateX());
        assertThat(result.getBody().getPickets().get(0).getCoordinateY()).isNotEqualTo(result2.getBody().getPickets().get(0).getCoordinateY());

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isNotEqualTo(result2.getBody().getPickets().get(1).getPicketInternalId());
        assertThat(result2.getBody().getPickets().get(1).getName()).isEqualTo("Picket2");
        assertThat(result2.getBody().getPickets().get(1).getCoordinateY()).isEqualTo(40);
        assertThat(result2.getBody().getPickets().get(1).getCoordinateX()).isEqualTo(30);
    }

    @Test
    public void httpPut_updateMeasurement_addOnePicketWithDuplicateName_throwException() {
        // given
        var measurementName = UUID.randomUUID().toString();

        var picketWriteModel1 = new PicketWriteModel();
        picketWriteModel1.setCoordinateX(1);
        picketWriteModel1.setCoordinateY(2);
        picketWriteModel1.setName("Picket1");

        var picketWriteModel2 = new PicketWriteModel();
        picketWriteModel2.setCoordinateX(3);
        picketWriteModel2.setCoordinateY(4);
        picketWriteModel2.setName("Picket2");

        // when
        ResponseEntity<MeasurementReadModel> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel1, picketWriteModel2)),
                        MeasurementReadModel.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        picketWriteModel1.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId());
        picketWriteModel1.setCoordinateX(2);
        picketWriteModel1.setCoordinateY(10);
        picketWriteModel1.setName("Picket10");

        picketWriteModel2.setPicketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(1).getPicketInternalId());

        var picketWriteModel = new PicketWriteModel();
        picketWriteModel.setCoordinateX(30);
        picketWriteModel.setCoordinateY(40);
        picketWriteModel.setName("Picket2");

        // when
        ResponseEntity<ViolationReport> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getId(),
                        this.createNewMeasurementRequestBody(measurementName, List.of(picketWriteModel1, picketWriteModel2, picketWriteModel)),
                        ViolationReport.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(result2.getBody()).getMessage()).isEqualTo("The measurement with given payload already exists!");
        assertThat(Objects.requireNonNull(result2.getBody()).getCause()).containsIgnoringCase("pk_name_measurement_id");
    }

    private MeasurementWriteModel createNewMeasurementRequestBody(String name, List<PicketWriteModel> pickets) {
        var measurementRequest = new MeasurementWriteModel();
        measurementRequest.setName(name);
        measurementRequest.setPlace("Gdansk");
        measurementRequest.setOwner("Kamil Demkowicz");
        measurementRequest.setPickets(pickets);

        return measurementRequest;
    }
}
