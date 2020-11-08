package points.transforming.app.server.services.picket;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import points.transforming.app.server.UnitTestWithMockito;
import points.transforming.app.server.models.picket.Picket;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTestWithMockito
public class CoordinatesConversionServiceTest {

    @InjectMocks
    private CoordinatesConversionService coordinatesConversionService;

    @Test
    public void shouldCalculateCoordinatesFrom2000ToWgs84Correctly() {
        // given
        var picket1 = new Picket();
        picket1.setName("picket1");
        picket1.setCoordinateX2000(5534190.87);
        picket1.setCoordinateY2000(6572693.19);
        picket1.setPicketInternalId("PIC-1");

        // when
        final var result = coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(picket1, 6);

        assertThat(result.getCoordinateX().doubleValue()).isEqualTo(49.93956789022778);
        assertThat(result.getCoordinateY().doubleValue()).isEqualTo(19.012730260613353);
    }

    @Test
    public void shouldCalculateCoordinateFromWgs84ToGeocentricCorrectly() {
        // given
        var picket1 = new Picket();
        picket1.setName("picket1");
        picket1.setLatitude(50.31040135858681);
        picket1.setLongitude(18.888575546891655);
        picket1.setPicketInternalId("PIC-1");

        // when
        final var result = coordinatesConversionService.convertCoordinateFromWgs84ToGeocentric(picket1, 6);

        assertThat(result.getCoordinateX().doubleValue()).isEqualTo(5575321.99);
        assertThat(result.getCoordinateY().doubleValue()).isEqualTo(6563290.64);
    }
}
