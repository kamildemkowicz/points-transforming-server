package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class V6__create_fixtures extends BaseJavaMigration {
    private final List<Measurement> measurements = new ArrayList<>();
    private final List<Picket> pickets = new ArrayList<>();
    Random r = new Random();

    private final List<String> cities = List.of(
            "Gdansk", "Warszawa", "Zielona Gora", "Krakow", "Gdynia", "Tczew", "Bydgoszcz", "Torun", "Czestochowa", "Lublin"
    );

    @Override
    public void migrate(Context context) {
        this.createMeasurements();
        this.createPickets();

        var insertMeasurement = "INSERT INTO measurement (name, creation_date, end_date, place) VALUES (?, ?, ?, ?)";
        var insertPickets = "INSERT INTO picket (picket_id, coordinateX, coordinateY, measurement_id) VALUES (?, ?, ?, ?)";

        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .batchUpdate(insertMeasurement, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, measurements.get(i).getName());
                        ps.setDate(2, Date.valueOf(measurements.get(i).getCreationDate().toLocalDate()));
                        ps.setDate(3, getEndDateIfExist(i));
                        ps.setString(4, measurements.get(i).getPlace());
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
                        ps.setString(1, pickets.get(i).getPicketId());
                        ps.setDouble(2, pickets.get(i).getCoordinateX());
                        ps.setDouble(3, pickets.get(i).getCoordinateY());
                        ps.setInt(4, pickets.get(i).getMeasurement().getId());
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
                    "place" + (i+1), measurementsId, this.cities.get(r.nextInt(10)));
            i++;
            measurementsId++;
        }

        i = 0;

        while(i < 100) {
            this.createNextVersionMeasurements(measurements.get(i).getEndDate(), LocalDateTime.now().minusDays(i+7),
                    "place" + (i+1), measurementsId, measurements.get(i).getPlace());
            i++;
            measurementsId++;
        }

        i = 0;

        while(i < 100) {
            this.createNextVersionMeasurements(measurements.get(i+100).getEndDate(), LocalDateTime.now().minusDays(i+4),
                    "place" + (i+1), measurementsId, measurements.get(i).getPlace());
            i++;
            measurementsId++;
        }

        i = 0;

        while(i < 100) {
            this.createNextVersionMeasurements(measurements.get(i+200).getEndDate(), null,
                    "place" + (i+1), measurementsId, measurements.get(i).getPlace());
            i++;
            measurementsId++;
        }
    }

    private void createNextVersionMeasurements(LocalDateTime creationDate, LocalDateTime endDate, String name, int measurementsId, String place) {
        var m = new Measurement();
        m.setId(measurementsId);
        m.setName(name);
        m.setPlace(place);
        m.setCreationDate(creationDate);
        m.setEndDate(endDate);
        this.measurements.add(m);
    }

    private void createPickets() {
        int i = 0;
        int measurementId = 0;
        double minX = 17.0000001;
        double maxX = 19.0000001;
        double minY = 55.0000001;
        double maxY = 58.0000001;

        while(i < 40000) {
            var p = new Picket();
            p.setPicketId("picket" + i);
            p.setCoordinateX(getNextDouble(minX, maxX));
            p.setCoordinateY(getNextDouble(minY, maxY));
            p.setMeasurement(this.measurements.get(measurementId));
            i++;
            pickets.add(p);

            if (i % 100 == 0) {
                measurementId++;
            }
        }
    }

    private double getNextDouble(double min, double max) {
        return min + (max - min) * r.nextDouble();
    }
}
