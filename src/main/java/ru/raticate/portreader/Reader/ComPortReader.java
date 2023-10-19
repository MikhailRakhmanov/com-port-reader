package ru.raticate.portreader.Reader;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Loggers.LoggerLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ComPortReader {


    Integer com;
    @Autowired
    String exitBarcode;
    private final Logger logger;


    public ComPortReader(Logger logger, Integer com) {
        this.logger = logger;
        this.com = com;

    }




    public Map<Integer, Set<Integer>> read() {
        Map<Integer, Set<Integer>> platform2product = new TreeMap<>(Comparator.naturalOrder());

        List<SerialPort> ports = new ArrayList<>(Arrays.stream(SerialPort.getCommPorts()).toList());

        if (ports.isEmpty()) {
            logger.log("No COM ports available.", LoggerLevel.File);
            return platform2product;
        }

        for (int i = 0; i < ports.size(); i++) {
            SerialPort serialPort = ports.get(i);
            if (!serialPort.openPort()) {
                ports.remove(serialPort);

            }
        }

        SerialPort port = ports.get(com);
        port.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, Integer.MAX_VALUE, 0);

        BufferedReader bufferedReader = null;
        int currentPlatform = -1;
        String value;
        int numValue = 0;
        while (currentPlatform == -1 && port.openPort()) {
            logger.log("Отсканируйте пирамиду",LoggerLevel.Browser);

            bufferedReader = new BufferedReader(new InputStreamReader(port.getInputStream()));

            try {
                value = bufferedReader.readLine();
                if (value.length() != 9) {
                    throw new Exception();
                }
                numValue = Integer.parseInt(value);
            } catch (Exception e) {
                logger.log("Это не 9-тизначное число",LoggerLevel.Browser);
            }

            if (100000000 < numValue && numValue < 100000100) {
                currentPlatform = numValue % 100;
                logger.log("Выбрана пирамида №" + currentPlatform,LoggerLevel.File,LoggerLevel.Browser,LoggerLevel.Console);
            }
        }
        while (port.openPort()) {
            value = exitBarcode;
            try {
                assert bufferedReader != null;
                value = bufferedReader.readLine();

            } catch (IOException e) {
                logger.log("Ошибка чтения",LoggerLevel.Browser,LoggerLevel.File);
            }
            if (value.equals(exitBarcode)) {
                logger.log("Пирамида -> изделие: " + platform2product);
                AtomicInteger sum = new AtomicInteger();
                platform2product.values().forEach(e -> e.forEach(o -> sum.getAndIncrement()));
                String resultInfo = "Отсканированно: " + sum.get() + " изделий.";
                logger.log(resultInfo,LoggerLevel.File,LoggerLevel.Browser,LoggerLevel.Console);

                break;
            }
            if (value.length() != 9) {
                logger.log("Неверный штрих :" + value,LoggerLevel.Browser,LoggerLevel.File,LoggerLevel.Console);
                continue;
            }
            try {

                numValue = Integer.parseInt(value);

            } catch (Exception e) {
                logger.log("code error",LoggerLevel.Browser,LoggerLevel.File,LoggerLevel.Console);
            }

            if (100000000 < numValue && numValue < 100000100) {
                currentPlatform = numValue % 100;
                logger.log("Выбрана пирамида №" + currentPlatform,LoggerLevel.Browser,LoggerLevel.File,LoggerLevel.Console);
            } else {
                final Integer[] platformToRemove = {null};
                int num = numValue / 1000;
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
                    platform2product.get(currentPlatform).add(numValue / 1000);
                } else {
                    Set<Integer> set = new TreeSet<>(Comparator.naturalOrder());
                    set.add(numValue / 1000);
                    platform2product.put(currentPlatform, set);
                }
                logger.log(value + ": " + currentPlatform,LoggerLevel.Browser,LoggerLevel.File,LoggerLevel.Console);
            }

        }
        port.closePort();
        return platform2product;
    }
}