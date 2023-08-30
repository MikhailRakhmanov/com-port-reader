package ru.raticate.portreader;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class ComPortReader {
    public static void main(String[] args) throws IOException {
        Map<Integer, Set<String>> platform2product = new TreeMap<>(Comparator.naturalOrder());

        List<SerialPort> ports = new java.util.ArrayList<>(Arrays.stream(SerialPort.getCommPorts()).toList());

        if (ports.size() == 0) {
            System.out.println("No COM ports available.");
            return;
        }

        for (int i = 0; i < ports.size(); i++) {
            SerialPort serialPort = ports.get(i);
            if (!serialPort.openPort()) {
                ports.remove(serialPort);
            }
        }


        SerialPort port = ports.get(0);
        port.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, Integer.MAX_VALUE, 0);
        System.out.println("Отсканируйте пирамиду");
        BufferedReader bufferedReader = null;
        int currentPlatform = -1;
        while (currentPlatform == -1 && port.openPort()) {
            InputStream inputStream = port.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            int value = Integer.parseInt(bufferedReader.readLine());
            if (value < 100) {
                currentPlatform = value;
                System.out.println("Выбрана пирамида №" + currentPlatform);
            }
        }

        while (port.openPort()) {
            String strValue = null;
            try {
                strValue = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int value = Integer.parseInt(strValue);
            if (value == 999) {
                System.out.println(platform2product);
                break;
            }
            if (value < 100) {
                currentPlatform = value;
                System.out.println("Выбрана пирамида №" + currentPlatform);
            } else {
                if (platform2product.containsKey(currentPlatform)) {
                    platform2product.get(currentPlatform).add(strValue);

                } else {
                    Set<String> set = new TreeSet<>(Comparator.naturalOrder());
                    set.add(strValue);
                    platform2product.put(currentPlatform, set);
                }
                System.out.println(strValue+": "+currentPlatform);
            }
        }
        port.closePort();
        System.out.println("Port closed.");

    }
}