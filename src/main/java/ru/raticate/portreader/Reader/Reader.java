package ru.raticate.portreader.Reader;

import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Loggers.LoggerLevel;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Reader {
    String exitBarcode;
    private final Logger logger;
    public Boolean isRead = false;

    public Map<Integer, Set<Integer>> platform2product = new TreeMap<>(Comparator.naturalOrder());

    protected Reader(Logger logger, String exitBarcode) {
        this.logger = logger;
        this.exitBarcode = exitBarcode;
    }

    public Map<Integer, Set<Integer>> startRead() {
        isRead = true;

        Integer currentPlatform = null;
        String value;

        logger.log("Отсканируйте пирамиду", LoggerLevel.Browser, LoggerLevel.Console);
        do {

            value = getValue();
            if (value == null) {
                continue;
            }
            if (value.equals(exitBarcode)) {
                System.out.println("end");
                break;
            }
            Integer integerValue;
            try {
                integerValue = Integer.parseInt(value);
            } catch (Exception e) {
                integerValue = null;
            }
            if (integerValue != null)
                if (0 <= integerValue && integerValue <= 215) {
                    currentPlatform = integerValue;
                    logger.log("Выбрана пирамида №" + currentPlatform, LoggerLevel.File, LoggerLevel.Browser, LoggerLevel.Console);
                } else if (currentPlatform != null) {
                    final Integer[] platformToRemove = {null};
                    int num = integerValue;
                    platform2product.forEach((key, value1) -> {
                        value1.remove(num);
                        if (value1.isEmpty()) {
                            platformToRemove[0] = key;
                        }
                    });
                    if (platformToRemove[0] != null) {
                        platform2product.remove(platformToRemove[0]);
                    }
                    if (platform2product.containsKey(currentPlatform)) {
                        platform2product.get(currentPlatform).add(integerValue);
                    } else {
                        Set<Integer> set = new TreeSet<>(Comparator.naturalOrder());
                        set.add(integerValue);
                        platform2product.put(currentPlatform, set);
                    }
                    logger.log(value + ": " + currentPlatform, LoggerLevel.Browser, LoggerLevel.File, LoggerLevel.Console);
                }

        } while (true);

        logger.log("Пирамида -> изделие: " + platform2product, LoggerLevel.File, LoggerLevel.Console);
        AtomicInteger sum = new AtomicInteger();
        platform2product.values().forEach(e -> e.forEach(o -> sum.getAndIncrement()));
        String resultInfo = "Отсканированно: " + sum.get() + " изделий.";
        logger.log(resultInfo, LoggerLevel.File, LoggerLevel.Browser, LoggerLevel.Console);
        isRead = false;
        return platform2product;
    }

    public Map<Integer, Set<Integer>> getCurrentData() {
        return platform2product;
    }

    abstract public String getValue();

    public void clearData() {
        isRead = false;
        platform2product = new TreeMap<>(Comparator.naturalOrder());
    }
}
