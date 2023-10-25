package ru.raticate.portreader.DBConnection;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.Loggers.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.Set;

public abstract class DBWriter {
    protected final double date;
    protected final Logger logger;
    protected final JdbcTemplate jdbcTemplate;

    public DBWriter(Logger logger, JdbcTemplate jdbcTemplate) {

        this.logger = logger;
        this.jdbcTemplate = jdbcTemplate;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        Duration duration = Duration.between(then, now);
        date = (double) duration.toSeconds()/86400;
        System.out.println(date);
    }
    abstract public void sendQuery(Map<Integer, Set<Integer>> platform2product);
}
