package points.transforming.app.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import points.transforming.app.server.ControllerTestWithMockito;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;
import points.transforming.app.server.models.measurement.MeasurementResponse;
import points.transforming.app.server.models.measurement.MeasurementRequest;
import points.transforming.app.server.models.picket.PicketRequest;
import points.transforming.app.server.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTestWithMockito
public class MeasurementControllersIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Test
    public void httpGet_returnsAllMeasurements() {
        // given
        var measurementNumber = this.measurementRepository.findAll().size();

        // when
        ResponseEntity<MeasurementResponse[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements", MeasurementResponse[].class);

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
        ResponseEntity<MeasurementResponse[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements?size=10" , MeasurementResponse[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(10);
    }

    @Test
    public void httpGet_returnsAllMeasurements_WithGivenSizeAndPage() {
        // given

        // when
        ResponseEntity<MeasurementResponse[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements?size=10&page=2" , MeasurementResponse[].class);

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
        ResponseEntity<MeasurementResponse[]> result= this.testRestTemplate
                .getForEntity("http://localhost:" + port + "/measurements?size=10&page=1&sort=place,asc" , MeasurementResponse[].class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(10);
        assertThat(result.getBody()[0].getPlace()).isEqualTo("Bydgoszcz");
    }

    @Test
    public void httpPost_createMeasurement_emptySetOfPickets() {
        // given
        final var measurementRequest = MeasurementRequest.builder()
            .name(UUID.randomUUID().toString())
            .owner("Kamil Demkowicz")
            .place("Gdansk")
            .districtId(20)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result= this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    measurementRequest,
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void httpPost_createMeasurementBadRequest_throwException() throws Exception {
        // given
        final var objectMapper = new ObjectMapper();
        final var optInBonusCreateRequest = Map.of(
            "name", "Kamil"
        );

        final String payload = objectMapper.writeValueAsString(optInBonusCreateRequest);

        // when
        final var requestBuilder = buildCreateMeasurementsCall(payload);

        // then
        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errors[0].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[0].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[0].field", is("owner")))
            .andExpect(jsonPath("errors[0].reasons[0].message", is("must not be blank")))
            .andExpect(jsonPath("errors[1].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[1].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[1].field", is("districtId")))
            .andExpect(jsonPath("errors[1].reasons[0].message", is("must not be null")))
            .andExpect(jsonPath("errors[2].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[2].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[2].field", is("place")))
            .andExpect(jsonPath("errors[2].reasons[0].message", is("must not be blank")));
    }

    @Test
    public void httpPost_createMeasurementWithoutPlaceAndNameAndOwner_throwExceptions() throws Exception {
        // given
        final var objectMapper = new ObjectMapper();
        final var optInBonusCreateRequest = Map.of();

        final String payload = objectMapper.writeValueAsString(optInBonusCreateRequest);

        // when
        final var requestBuilder = buildCreateMeasurementsCall(payload);

        // then
        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errors[0].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[0].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[0].field", is("owner")))
            .andExpect(jsonPath("errors[0].reasons[0].message", is("must not be blank")))
            .andExpect(jsonPath("errors[1].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[1].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[1].field", is("districtId")))
            .andExpect(jsonPath("errors[1].reasons[0].message", is("must not be null")))
            .andExpect(jsonPath("errors[2].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[2].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[2].field", is("name")))
            .andExpect(jsonPath("errors[2].reasons[0].message", is("must not be blank")))
            .andExpect(jsonPath("errors[3].message", is("Request has invalid data")))
            .andExpect(jsonPath("errors[3].errorCode", is("PTS002")))
            .andExpect(jsonPath("errors[3].field", is("place")))
            .andExpect(jsonPath("errors[3].reasons[0].message", is("must not be blank")));
    }

    @Test
    public void httpPost_createMeasurementWithPickets() {
        // given
        final var measurementName = UUID.randomUUID().toString();
        final var time = LocalDateTime.now();
        final var picket1 = PicketRequest.builder()
            .name("picket1")
            .coordinateX2000(40.30)
            .coordinateY2000(42.30)
            .build();

        final var picket2 = PicketRequest.builder()
            .name("picket2")
            .coordinateX2000(30.30)
            .coordinateY2000(32.30)
            .build();

        final var measurementRequest = MeasurementRequest.builder()
            .name(measurementName)
            .place("Gdansk")
            .owner("Kamil Demkowicz")
            .districtId(20)
            .pickets(List.of(picket1, picket2))
            .build();

        // when
        final ResponseEntity<MeasurementResponse> result= this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    measurementRequest,
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(result.getBody()).getPickets().size()).isEqualTo(2);
        assertThat(result.getBody().getPlace()).isEqualTo("Gdansk");
        assertThat(result.getBody().getName()).isEqualTo(measurementName);
        assertThat(result.getBody().getOwner()).isEqualTo("Kamil Demkowicz");
        assertThat(result.getBody().getDistrict().getName()).isEqualTo("mysliborski");
        assertThat(result.getBody().getCreationDate()).isNotNull().isAfter(time);
    }

    @Test
    public void httpPost_createMeasurementWithTheSameNameButDifferentInternalIds() {
        // given
        var measurementName = UUID.randomUUID().toString();

        final var measurementRequest = MeasurementRequest.builder()
            .name(measurementName)
            .place("Gdansk")
            .owner("Kamil Demkowicz")
            .districtId(20)
            .build();

        // when
        final ResponseEntity<MeasurementResponse> result= this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    measurementRequest,
                    MeasurementResponse.class);

        String measurementInternalId1 = this.measurementRepository
                .findById(Objects.requireNonNull(result.getBody()).getId())
                .get()
                .getMeasurementInternalId();

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // when
        ResponseEntity<MeasurementResponse> result2 = this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    measurementRequest,
                    MeasurementResponse.class);

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
        var picket1 = PicketRequest.builder()
            .name("picket1")
            .coordinateX2000(40.30)
            .coordinateY2000(42.30)
            .build();

        var picket2 = PicketRequest.builder()
            .name("picket1")
            .coordinateX2000(30.30)
            .coordinateY2000(32.30)
            .build();

        final var measurementRequest = MeasurementRequest.builder()
            .name(measurementName)
            .place("Gdansk")
            .owner("Kamil Demkowicz")
            .districtId(20)
            .pickets(List.of(picket1, picket2))
            .build();

        // when
        ResponseEntity<PointsTransformingErrorResponse> result = this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    measurementRequest,
                    PointsTransformingErrorResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void httpPut_createTwoMeasurementVersions_WithoutChanges() {
        // given
        final var measurementName = UUID.randomUUID().toString();

        final var picket1 = PicketRequest.builder()
            .name("picket1")
            .coordinateX2000(40.30)
            .coordinateY2000(42.30)
            .build();

        final var picket2 = PicketRequest.builder()
            .name("picket2")
            .coordinateX2000(40.30)
            .coordinateY2000(42.30)
            .build();

        final var measurementRequest = MeasurementRequest.builder()
            .name(measurementName)
            .place("Gdansk")
            .owner("Kamil Demkowicz")
            .districtId(20)
            .pickets(List.of(picket1, picket2))
            .build();

        // when
        final ResponseEntity<MeasurementResponse> result = this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    measurementRequest,
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        final var picket1_2 = PicketRequest.builder()
            .name("picket1")
            .coordinateX2000(40.30)
            .coordinateY2000(42.30)
            .build();

        final var picket2_2 = PicketRequest.builder()
            .name("picket2")
            .coordinateX2000(40.30)
            .coordinateY2000(42.30)
            .build();

        final var measurementRequest2 = MeasurementRequest.builder()
            .name(measurementName)
            .place("Gdansk")
            .owner("Kamil Demkowicz")
            .districtId(20)
            .pickets(List.of(picket1_2, picket2_2))
            .build();

        // when
        ResponseEntity<MeasurementResponse> result2 = this.testRestTemplate
            .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getMeasurementInternalId(),
                measurementRequest2,
                MeasurementResponse.class
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
        assertThat(result.getBody().getPickets().get(0).getLatitude()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getLatitude());
        assertThat(result.getBody().getPickets().get(0).getLongitude()).isEqualTo(Objects.requireNonNull(result2.getBody()).getPickets().get(0).getLongitude());
    }

    @Test
    public void httpPut_updateMeasurement_ChangeMetaProperties() {
        // given
        var measurementName = UUID.randomUUID().toString();

        // when
        ResponseEntity<MeasurementResponse> result = this.testRestTemplate
                .postForEntity(
                        "http://localhost:" + port + "/measurements",
                        this.createNewMeasurementRequestBody(measurementName, List.of()),
                        MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        final var measurementRequest = MeasurementRequest.builder()
            .name("nowaNazwa")
            .place("Zielona Góra")
            .owner("Mariusz Kaczmarek")
            .districtId(20)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result2 = this.testRestTemplate
            .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getMeasurementInternalId(),
                measurementRequest,
                MeasurementResponse.class
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
        ResponseEntity<MeasurementResponse> result = this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    this.createNewMeasurementRequestBody(measurementName, List.of()),
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        final var picketRequest = PicketRequest.builder()
            .name("NowaPikieta")
            .latitude(30)
            .longitude(40)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result2 = this.testRestTemplate
            .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getMeasurementInternalId(),
                this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest)),
                MeasurementResponse.class
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

        final var picketRequest1 = PicketRequest.builder()
            .name("Picket1")
            .coordinateX2000(1)
            .coordinateY2000(2)
            .build();

        final var picketRequest2 = PicketRequest.builder()
            .name("Picket2")
            .coordinateX2000(3)
            .coordinateY2000(4)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result = this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest1, picketRequest2)),
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        final var picketRequest1_2 = PicketRequest.builder()
            .name("Picket10")
            .coordinateX2000(2)
            .coordinateY2000(10)
            .picketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId())
            .build();

        final var picketRequest2_2 = PicketRequest.builder()
            .name("Picket2")
            .coordinateX2000(3)
            .coordinateY2000(4)
            .picketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(1).getPicketInternalId())
            .build();

        final var picketRequest = PicketRequest.builder()
            .name("NowaPikieta")
            .latitude(30)
            .longitude(40)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getMeasurementInternalId(),
                    this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest1_2, picketRequest2_2, picketRequest)),
                    MeasurementResponse.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody().getPickets().size()).isEqualTo(2);
        assertThat(Objects.requireNonNull(result2.getBody()).getPickets().size()).isEqualTo(3);

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isEqualTo(result2.getBody().getPickets().get(0).getPicketInternalId());
        assertThat(result.getBody().getPickets().get(0).getId()).isNotEqualTo(result2.getBody().getPickets().get(0).getId());
        assertThat(result.getBody().getPickets().get(0).getName()).isNotEqualTo(result2.getBody().getPickets().get(0).getName());
        assertThat(result.getBody().getPickets().get(0).getLatitude()).isNotEqualTo(result2.getBody().getPickets().get(0).getLatitude());
        assertThat(result.getBody().getPickets().get(0).getLongitude()).isNotEqualTo(result2.getBody().getPickets().get(0).getLongitude());

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isNotEqualTo(result2.getBody().getPickets().get(2).getPicketInternalId());
        assertThat(result2.getBody().getPickets().get(2).getName()).isEqualTo("NowaPikieta");
        assertThat(result2.getBody().getPickets().get(2).getLongitude()).isEqualTo(40);
        assertThat(result2.getBody().getPickets().get(2).getLatitude()).isEqualTo(30);
    }

    @Test
    public void httpPut_updateMeasurement_addOnePicket_and_removeExistOne() {
        // given
        var measurementName = UUID.randomUUID().toString();

        final var picketRequest1 = PicketRequest.builder()
            .name("Picket1")
            .coordinateX2000(1)
            .coordinateY2000(2)
            .build();

        final var picketRequest2 = PicketRequest.builder()
            .name("Picket2")
            .coordinateX2000(3)
            .coordinateY2000(4)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result = this.testRestTemplate
                .postForEntity(
                    "http://localhost:" + port + "/measurements",
                    this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest1, picketRequest2)),
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        final var picketRequest1_2 = PicketRequest.builder()
            .name("Picket10")
            .coordinateX2000(2)
            .coordinateY2000(10)
            .picketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId())
            .build();

        final var picketRequest = PicketRequest.builder()
            .name("Picket2")
            .latitude(30)
            .longitude(40)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getMeasurementInternalId(),
                    this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest1_2, picketRequest)),
                    MeasurementResponse.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody().getPickets().size()).isEqualTo(2);
        assertThat(Objects.requireNonNull(result2.getBody()).getPickets().size()).isEqualTo(2);

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isEqualTo(result2.getBody().getPickets().get(0).getPicketInternalId());
        assertThat(result.getBody().getPickets().get(0).getId()).isNotEqualTo(result2.getBody().getPickets().get(0).getId());
        assertThat(result.getBody().getPickets().get(0).getName()).isNotEqualTo(result2.getBody().getPickets().get(0).getName());
        assertThat(result.getBody().getPickets().get(0).getLatitude()).isNotEqualTo(result2.getBody().getPickets().get(0).getLatitude());
        assertThat(result.getBody().getPickets().get(0).getLongitude()).isNotEqualTo(result2.getBody().getPickets().get(0).getLongitude());

        assertThat(result.getBody().getPickets().get(0).getPicketInternalId()).isNotEqualTo(result2.getBody().getPickets().get(1).getPicketInternalId());
        assertThat(result2.getBody().getPickets().get(1).getName()).isEqualTo("Picket2");
        assertThat(result2.getBody().getPickets().get(1).getLongitude()).isEqualTo(40);
        assertThat(result2.getBody().getPickets().get(1).getLatitude()).isEqualTo(30);
    }

    @Test
    public void httpPut_updateMeasurement_addOnePicketWithDuplicateName_throwException() {
        // given
        var measurementName = UUID.randomUUID().toString();

        final var picketRequest1 = PicketRequest.builder()
            .name("Picket1")
            .coordinateX2000(1)
            .coordinateY2000(2)
            .build();

        final var picketRequest2 = PicketRequest.builder()
            .name("Picket2")
            .coordinateX2000(3)
            .coordinateY2000(4)
            .build();

        // when
        ResponseEntity<MeasurementResponse> result = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements",
                    this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest1, picketRequest2)),
                    MeasurementResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // given
        final var picketRequest1_2 = PicketRequest.builder()
            .name("Picket10")
            .coordinateX2000(2)
            .coordinateY2000(10)
            .picketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(0).getPicketInternalId())
            .build();

        final var picketRequest2_2 = PicketRequest.builder()
            .name("Picket2")
            .coordinateX2000(3)
            .coordinateY2000(4)
            .picketInternalId(Objects.requireNonNull(result.getBody()).getPickets().get(1).getPicketInternalId())
            .build();

        final var picketRequest = PicketRequest.builder()
            .name("Picket2")
            .latitude(30)
            .longitude(40)
            .build();

        // when
        ResponseEntity<PointsTransformingErrorResponse> result2 = this.testRestTemplate
                .postForEntity("http://localhost:" + port + "/measurements/" + Objects.requireNonNull(result.getBody()).getMeasurementInternalId(),
                    this.createNewMeasurementRequestBody(measurementName, List.of(picketRequest1_2, picketRequest2_2, picketRequest)),
                    PointsTransformingErrorResponse.class
                );

        // then
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private MeasurementRequest createNewMeasurementRequestBody(String name, List<PicketRequest> pickets) {
        return MeasurementRequest.builder()
            .name(name)
            .place("Gdansk")
            .owner("Kamil Demkowicz")
            .districtId(20)
            .pickets(pickets)
            .build();
    }

    private MockHttpServletRequestBuilder buildCreateMeasurementsCall(final String payload) {
        return MockMvcRequestBuilders.post("http://localhost:" + port + "/measurements")
            .content(payload)
            .contentType(APPLICATION_JSON_VALUE)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
    }
}
