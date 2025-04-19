package t1.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:25432/postgres");
        hikariConfig.setUsername("test");
        hikariConfig.setPassword("test");
        return new HikariDataSource(hikariConfig);
    }
}
