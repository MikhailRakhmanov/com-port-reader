package ru.raticate.portreader.Reader;

import com.fazecast.jSerialComm.SerialPort;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Loggers.LoggerLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComPortReader extends Reader {
    BufferedReader bufferedReader;
    Logger logger;


    public ComPortReader(Logger logger, Integer com, String exitBarcode) {
        super(logger, exitBarcode);
        this.exitBarcode = exitBarcode;
        List<SerialPort> ports = new ArrayList<>(Arrays.stream(SerialPort.getCommPorts()).toList());
        for (int i = 0; i < ports.size(); i++) {
            SerialPort serialPort = ports.get(i);
            if (!serialPort.openPort()) {
                ports.remove(serialPort);
            }
        }
        SerialPort serialPort = ports.get(com);
        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, Integer.MAX_VALUE, 0);
        bufferedReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
    }


    @Override
    public String getValue() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            logger.log("Ошибка чтения COM-port", LoggerLevel.Console,LoggerLevel.File,LoggerLevel.Browser);
        }
        return null;
    }


}