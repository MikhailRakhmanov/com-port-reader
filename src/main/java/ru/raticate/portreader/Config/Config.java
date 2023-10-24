package ru.raticate.portreader.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DBConnection.DatabaseWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.ComPortReader;
import ru.raticate.portreader.Reader.KeyboardReader;
import ru.raticate.portreader.Reader.Reader;

@Configuration
public class Config {

    final
    JdbcTemplate jdbcTemplate;

    @Autowired
    public Config(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${reader.type}")
    String readerType;

    @Value("${reader.com}")
    Integer com;

    @Value("${reader.exit-barcode}")
    String exitBarcode;

    @Bean
    String exitBarcode() {
        return exitBarcode;
    }

    @Bean
    DatabaseWriter databaseWriter() {
        return new DatabaseWriter(logger(), jdbcTemplate);
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
