package ru.raticate.portreader.Reader;

import javafx.util.Pair;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Loggers.LoggerLevel;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Reader {
    public boolean isEnd = false;
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
//        isRead = true;
//
//        Integer currentPlatform = null;
//        String value;
//
//        logger.log("Отсканируйте пирамиду", LoggerLevel.Browser, LoggerLevel.Console);
//        do {
//
//            value = getValue();
//            if (value == null) {
//                continue;
//            }
//            if (value.equals(exitBarcode)) {
//                isEnd = true;
//                break;
//            }
//            Integer integerValue;
//            try {
//                integerValue = Integer.parseInt(value);
//            } catch (Exception e) {
//                integerValue = null;
//            }
//            if (integerValue != null)
//                if (0 <= integerValue && integerValue <= 215) {
//                    currentPlatform = integerValue;
//                    logger.log("Выбрана пирамида №" + currentPlatform, LoggerLevel.File, LoggerLevel.Browser, LoggerLevel.Console);
//                } else if (currentPlatform != null) {
//                    final Integer[] platformToRemove = {null};
//                    int num = integerValue;
//                    platform2product.forEach((key, value1) -> {
//                        value1.remove(num);
//                        if (value1.isEmpty()) {
//                            platformToRemove[0] = key;
//                        }
//                    });
//                    if (platformToRemove[0] != null) {
//                        platform2product.remove(platformToRemove[0]);
//                    }
//                    if (platform2product.containsKey(currentPlatform)) {
//                        platform2product.get(currentPlatform).add(integerValue);
//                    } else {
//                        Set<Integer> set = new TreeSet<>(Comparator.naturalOrder());
//                        set.add(integerValue);
//                        platform2product.put(currentPlatform, set);
//                    }
//                    logger.log(value + ": " + currentPlatform, LoggerLevel.Browser, LoggerLevel.File, LoggerLevel.Console);
//                }
//
//        } while (true);
//
//        logger.log("Пирамида -> изделие: " + platform2product, LoggerLevel.File, LoggerLevel.Console);
//        AtomicInteger sum = new AtomicInteger();
//        platform2product.values().forEach(e -> e.forEach(o -> sum.getAndIncrement()));
//        String resultInfo = "Отсканированно: " + sum.get() + " изделий.";
//        logger.log(resultInfo, LoggerLevel.File, LoggerLevel.Browser, LoggerLevel.Console);
//        isRead = false;
//        return platform2product;
//    }

//    public Map<Integer, Set<Integer>> getCurrentData() {

        return new HashMap<Integer, Set<Integer>>();
    }

    abstract public String getValue();
}
