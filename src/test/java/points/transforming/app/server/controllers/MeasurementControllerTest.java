package points.transforming.app.server.controllers;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import points.transforming.app.server.ControllerTestWithMockito;
import points.transforming.app.server.repositories.MeasurementRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTestWithMockito
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeasurementRepository measurementRepository;

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

    private MockHttpServletRequestBuilder buildCreateMeasurementsCall(final String payload) {
        return MockMvcRequestBuilders.post("/measurements")
            .content(payload)
            .contentType(APPLICATION_JSON_VALUE)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
    }
}
