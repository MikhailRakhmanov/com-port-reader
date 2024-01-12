package ru.raticate.portreader.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DBConnection.DBWriter;
import ru.raticate.portreader.DBConnection.SPDataBaseWriter;


@Configuration
public class Config {


    @Bean
    DBWriter databaseWriter(@Qualifier("sPJdbcTemplate") JdbcTemplate jdbcTemplate) {
            return new SPDataBaseWriter(jdbcTemplate);
    }

}
