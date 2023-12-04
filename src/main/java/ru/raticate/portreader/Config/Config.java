package ru.raticate.portreader.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DBConnection.DBWriter;
import ru.raticate.portreader.DBConnection.DatabaseWriter;
import ru.raticate.portreader.DBConnection.SPDataBaseWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.ComPortReader;
import ru.raticate.portreader.Reader.KeyboardReader;
import ru.raticate.portreader.Reader.Reader;

@Configuration
public class Config {

    final
    JdbcTemplate jdbcTemplate;


    @Value("${reader.type}")
    private String readerType;

    @Value("${reader.com}")
    private Integer com;

    @Value("${reader.exit-barcode}")
    private String exitBarcode;

    @Value("${production}")
    private String production;

    public Config(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    String exitBarcode() {
        return exitBarcode;
    }
    @Bean
    String production() {
        return production;
    }

    @Bean
    DBWriter databaseWriter() {
        if (production.equalsIgnoreCase("sp")) {
            return new SPDataBaseWriter(jdbcTemplate, logger());
        } else if (production.equalsIgnoreCase("main")) {
            return new DatabaseWriter(jdbcTemplate, logger());
        }
        return new DatabaseWriter(jdbcTemplate, logger());

    }


    @Bean
    Logger logger() {
        return new Logger();
    }

    @Bean
    Reader reader() {
        if (readerType.equals("keyboardReader")) {
            return new KeyboardReader(logger(), exitBarcode());
        }
        if (readerType.equals("comPortReader")) {
            return new ComPortReader(logger(), com, exitBarcode());
        }
        return new KeyboardReader(logger(), exitBarcode());
    }
}
