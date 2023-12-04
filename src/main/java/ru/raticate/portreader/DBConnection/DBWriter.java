package ru.raticate.portreader.DBConnection;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DateConvertor;
import ru.raticate.portreader.Loggers.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.Set;

public abstract class DBWriter {
    protected final Logger logger;
    protected final JdbcTemplate jdbcTemplate;

    @Autowired

    public DBWriter(JdbcTemplate jdbcTemplate, Logger logger) {
        this.logger = logger;
        this.jdbcTemplate = jdbcTemplate;
    }

    abstract public void sendQuery(Map<Integer, Set<Integer>> platform2product);

    abstract public void sendQuery(Pair<Integer, Integer> platformAndProduct);
}
