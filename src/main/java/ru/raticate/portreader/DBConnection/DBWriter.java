package ru.raticate.portreader.DBConnection;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.Set;

public abstract class DBWriter {
    protected final JdbcTemplate sPJdbcTemplate;
    protected final JdbcTemplate mainJdbcTemplate;

    @Autowired

    public DBWriter(@Qualifier("sPJdbcTemplate") JdbcTemplate sPJdbcTemplate,@Qualifier("mainJdbcTemplate") JdbcTemplate mainJdbcTemplate) {
        this.sPJdbcTemplate = sPJdbcTemplate;
        this.mainJdbcTemplate = mainJdbcTemplate;
    }

    abstract public void sendQuery(Map<Integer, Set<Integer>> platform2product);

    abstract public void sendQuery(Pair<Integer, Integer> platformAndProduct);
}
