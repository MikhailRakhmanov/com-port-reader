package ru.raticate.portreader.DBConnection;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired

    public DBWriter(JdbcTemplate jdbcTemplate, Logger logger) {
        this.logger = logger;
        this.jdbcTemplate = jdbcTemplate;
        LocalDateTime now = LocalDateTime.now();
        date = dateToDouble(LocalDateTime.now());
    }

    public double dateToDouble(LocalDateTime now) {
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        Duration duration = Duration.between(then, now.plusDays(2));
        return (double) duration.toSeconds() / 86400;
    }

    public LocalDateTime doubleToDate(double date) {
        Duration duration = Duration.ofDays((long) date);
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        LocalDateTime now = then.plus(duration);
        return now;
    }

    abstract public void sendQuery(Map<Integer, Set<Integer>> platform2product);

    abstract public void sendQuery(Pair<Integer, Integer> platformAndProduct);
}
