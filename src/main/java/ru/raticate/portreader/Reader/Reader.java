package ru.raticate.portreader.Reader;

import javafx.util.Pair;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Loggers.LoggerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Reader {

    public boolean isEnd;
    String exitBarcode;
    protected final Logger logger;
    public Boolean isRead = false;
    public Integer currPlatform;


    protected Reader(Logger logger, String exitBarcode) {
        this.logger = logger;
        this.exitBarcode = exitBarcode;
    }

    public Pair<Integer, Integer> getCurrentPlatformAndProduct() {
        isRead = true;
        Integer product = null;
        Integer value = null;
        while (currPlatform == null || product == null) {
            try {
                value = Integer.parseInt(getValue());
            } catch (Exception ex) {
                logger.log("Неопознанный штрих код", LoggerLevel.File, LoggerLevel.Console, LoggerLevel.Browser);
                continue;
            }
            if (0 <= value && value <= 215 || value == 666) {
                logger.log("Пирамида: "+value,LoggerLevel.Console,LoggerLevel.Browser);
                currPlatform = value;
            } else if (currPlatform != null) {
                logger.log("Изделие: "+value,LoggerLevel.Console,LoggerLevel.Browser);
                product = value;
            } else {
                logger.log("Reader error",LoggerLevel.Console);
                return new Pair<>(null,null);
            }

        }
        return new Pair<>(currPlatform, product);
    }

    public Map<Integer, Set<Integer>> startRead() {
        return new HashMap<Integer, Set<Integer>>();
    }

    abstract public String getValue();
}
