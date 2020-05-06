package points.transforming.app.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    DataSource itTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:mysql://localhost:3306/points_transforming_it?serverTimezone=UTC", "root", "root");
        result.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return result;
    }
}
