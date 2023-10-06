package ru.raticate.portreader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class Config {

    final
    JdbcTemplate jdbcTemplate;

    @Value("${com}")
    String com;

    public Config(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Bean
    Logger logger(){
        return new Logger();
    }
    @Bean
    ComPortReader comPortReader(){
        ComPortReader comPortReader = new ComPortReader(jdbcTemplate,logger());
        comPortReader.sendQuery(comPortReader.read(com));
        return comPortReader;
    }
}
