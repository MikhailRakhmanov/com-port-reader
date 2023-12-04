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


    public ComPortReader(Logger logger, Integer com, String exitBarcode) {
        super(logger, exitBarcode);
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
        System.out.println(serialPort.getCTS());
        System.out.println(serialPort.getPortLocation());
        System.out.println(serialPort.getDCD());
        System.out.println(serialPort.getDSR());
        System.out.println(serialPort.getCTS());
        System.out.println(serialPort.getDeviceReadBufferSize());
        System.out.println(serialPort.getRTS());
        System.out.println(serialPort.getRI());
        System.out.println(serialPort.getSerialNumber());

        bufferedReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
    }


    @Override
    public String getValue() {
        String value = null;
        try {
            value = bufferedReader.readLine();
            if (value.equalsIgnoreCase(exitBarcode)) {
                return null;
            } else {
                return value;
            }
        } catch (IOException e) {
            logger.log("Ошибка чтения COM-port", LoggerLevel.Console);
        }
        return null;
    }

    public static void main(String[] args) {
        ComPortReader comPortReader = new ComPortReader(new Logger(), 1, "123");
        System.out.println(comPortReader.getValue());
    }


}