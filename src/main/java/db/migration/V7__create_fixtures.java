package db.migration;

import org.apache.commons.lang.math.RandomUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import points.transforming.app.server.models.measurement.District;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class V7__create_fixtures extends BaseJavaMigration {
    private final List<Measurement> measurements = new ArrayList<>();
    private final List<Picket> pickets = new ArrayList<>();
    private final District district = new District();

    Random r = new Random();

    private final List<String> cities = List.of(
            "Gdansk", "Warszawa", "Zielona Gora", "Krakow", "Gdynia", "Tczew", "Bydgoszcz", "Torun", "Czestochowa", "Lublin"
    );

    private final List<String> owners = List.of(
            "Kamil Demkowicz", "Mariusz Kaczmarek", "Joanna Florczyk", "Dawid Filipiak", "Grzegorz Kalinowski",
            "Dariusz Demkowicz", "Andrzej Strzelba", "Marian Kowalik", "Aleksander Liczyk", "Wojciech Grzelczak"
    );

    @Override
    public void migrate(Context context) {
        this.createMeasurements();
        this.createPickets();

        var insertMeasurement = "INSERT INTO measurement (measurement_internal_id, name, creation_date, end_date, place, owner, version, user_id, district_id)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        var insertPickets = "INSERT INTO picket (picket_internal_id, name, latitude, longitude, coordinate_x_2000, coordinate_y_2000, measurement_id)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";

        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .batchUpdate(insertMeasurement, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, measurements.get(i).getMeasurementInternalId());
                        ps.setString(2, measurements.get(i).getName());
                        ps.setDate(3, Date.valueOf(measurements.get(i).getCreationDate().toLocalDate()));
                        ps.setDate(4, getEndDateIfExist(i));
                        ps.setString(5, measurements.get(i).getPlace());
                        ps.setString(6, measurements.get(i).getOwner());
                        ps.setInt(7, measurements.get(i).getVersion());
                        ps.setInt(8, 1);
                        ps.setInt(9, measurements.get(i).getDistrict().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return measurements.size();
                    }

                    private Date getEndDateIfExist(int i) {
                        if (measurements.get(i).getEndDate() != null) {
                            return Date.valueOf(measurements.get(i).getEndDate().toLocalDate());
                        }

                        return null;
                    }
                });

        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .batchUpdate(insertPickets, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, pickets.get(i).getPicketInternalId());
                        ps.setString(2, pickets.get(i).getName());
                        ps.setDouble(3, pickets.get(i).getLatitude());
                        ps.setDouble(4, pickets.get(i).getLongitude());
                        ps.setDouble(5, pickets.get(i).getCoordinateX2000());
                        ps.setDouble(6, pickets.get(i).getCoordinateY2000());
                        ps.setInt(7, pickets.get(i).getMeasurement().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return pickets.size();
                    }
                });
    }

    private void createMeasurements() {
        int i = 0;
        int measurementsId = 1;

        while(i < 100) {
            this.createNextVersionMeasurements(LocalDateTime.now().minusDays(i+16), LocalDateTime.now().minusDays(i+10),
                    "place" + (i+1), 1, measurementsId, this.cities.get(r.nextInt(10)),
                    this.owners.get(r.nextInt(10)), "MES-" + (i+1));
            i++;
            measurementsId++;
        }

        i = 0;

        while(i < 100) {
            this.createNextVersionMeasurements(measurements.get(i).getEndDate(), LocalDateTime.now().minusDays(i+7),
                    "place" + (i+1), 2, measurementsId, measurements.get(i).getPlace(),
                    measurements.get(i).getOwner(), "MES-" + (i+1));
            i++;
            measurementsId++;
        }

        i = 0;

        while(i < 100) {
            this.createNextVersionMeasurements(measurements.get(i+100).getEndDate(), LocalDateTime.now().minusDays(i+4),
                    "place" + (i+1), 3, measurementsId, measurements.get(i).getPlace(),
                    measurements.get(i).getOwner(), "MES-" + (i+1));
            i++;
            measurementsId++;
        }

        i = 0;

        while(i < 100) {
            this.createNextVersionMeasurements(measurements.get(i+200).getEndDate(), null,
                    "place" + (i+1), 4, measurementsId, measurements.get(i).getPlace(),
                    measurements.get(i).getOwner(), "MES-" + (i+1));
            i++;
            measurementsId++;
        }
    }

    private void createNextVersionMeasurements(LocalDateTime creationDate, LocalDateTime endDate, String name,
                                               int version, int measurementsId, String place, String owner,
                                               String measurementInternalId) {
        var m = new Measurement();
        m.setId(measurementsId);
        m.setMeasurementInternalId(measurementInternalId);
        m.setVersion(version);
        m.setName(name);
        m.setOwner(owner);
        m.setPlace(place);
        m.setCreationDate(creationDate);
        m.setEndDate(endDate);
        district.setId(RandomUtils.nextInt(350));
        m.setDistrict(district);
        this.measurements.add(m);
    }

    private void createPickets() {
        int i = 0;
        int picketInternalId = 1;
        int measurementId = 0;

        double minX = 55.0000001;
        double maxX = 58.0000001;
        double minY = 17.0000001;
        double maxY = 19.0000001;


        final var minX2000 = BigDecimal.valueOf(5100000.00);
        final var maxX2000 = BigDecimal.valueOf(5535000.00);

        final var minY2000 = BigDecimal.valueOf(5500000.00);
        final var maxY2000 = BigDecimal.valueOf(8500000.00);

        while(i < 40000) {
            var p = new Picket();
            p.setPicketInternalId("PIC-" + picketInternalId);
            p.setName("picket" + i);
            p.setLatitude(getNextDouble(minX, maxX));
            p.setLongitude(getNextDouble(minY, maxY));
            p.setCoordinateX2000(getNextDoubleFor2000(minX2000, maxX2000));
            p.setCoordinateY2000(getNextDoubleFor2000(minY2000, maxY2000));
            p.setMeasurement(this.measurements.get(measurementId));
            i++;
            picketInternalId++;
            pickets.add(p);

            if (i % 100 == 0) {
                measurementId++;
                picketInternalId = 0;
            }
        }
    }

    private double getNextDouble(final double min, final double max) {
        return min + (max - min) * r.nextDouble();
    }

    private double getNextDoubleFor2000(final BigDecimal min, final BigDecimal max) {
        return max.subtract(min).multiply(BigDecimal.valueOf(r.nextDouble())).add(min).setScale(2, RoundingMode.CEILING).doubleValue();
    }
}
