package ru.raticate.portreader.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DBConnection.DatabaseWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.ComPortReader;

@Configuration
public class Config {

    final
    JdbcTemplate jdbcTemplate;

    @Autowired
    public Config(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Value("${com}")
    Integer com;

    @Bean
    DatabaseWriter databaseWriter() {
        return new DatabaseWriter(logger(), jdbcTemplate);
    }

    @Bean
    Logger logger() {
        return new Logger();
    }

    @Bean
    ComPortReader comPortReader() {
        return new ComPortReader(logger(), com);
    }
}
