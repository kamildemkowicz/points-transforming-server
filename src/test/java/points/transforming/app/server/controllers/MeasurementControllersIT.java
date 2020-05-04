package points.transforming.app.server.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.MeasurementReadModel;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasurementControllersIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    public static void init() {
        int i = 0;

        while(i < 5) {
            var measurement = new Measurement();
            measurement.setName("Measurement" + i);
            measurement.setPlace("Place" + i);
            i++;
        }
    }

    @Test
    public void findAllTestIT() {

        ResponseEntity<MeasurementReadModel[]> result= this.testRestTemplate
                .getForEntity("http://localhost:"+port+"/measurements", MeasurementReadModel[].class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        // assertThat(result.getBody(), is(notNullValue()));
    }
}
