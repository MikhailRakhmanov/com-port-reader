package ru.raticate.portreader;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Component;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
@Component
public class ComPortReader {
    File file = new File("history.txt");
    FileWriter fileWriter;
    ComPortReader(){
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            fileWriter = new FileWriter(file,true);
            fileWriter.write(localDateTime.toString()+'\n');
        } catch (IOException e) {
            System.err.println("Проблема записи файла истории");
        }

        if(!file.exists()) {
            try {
                 file.createNewFile();
            } catch (IOException e) {
                System.err.println("Проблема создания файла истории");
            }
        }
    }
    public void write(String content) {
        try {
            System.out.println(content);
            fileWriter.write(content+'\n');
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Проблема записи файла истории");
        }



    }
    public static void main(String[] args) {
        new ComPortReader().read("1");
    }

    public void read(String com) {
        Map<Long, Set<String>> platform2product = new TreeMap<>(Comparator.naturalOrder());
      
        List<SerialPort> ports = new java.util.ArrayList<>(Arrays.stream(SerialPort.getCommPorts()).toList());

        if (ports.size() == 0) {
            write("No COM ports available.");
            return;
        }

        for (int i = 0; i < ports.size(); i++) {
            SerialPort serialPort = ports.get(i);
            if (!serialPort.openPort()) {
                ports.remove(serialPort);

            }
        }

        SerialPort port = ports.get(Integer.parseInt(com));
        port.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, Integer.MAX_VALUE, 0);

        BufferedReader bufferedReader = null;
        long currentPlatform = -1;
        while (currentPlatform == -1 && port.openPort()) {
            write("Отсканируйте пирамиду");
            InputStream inputStream = port.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            int value = 0;
            try {
                value = Integer.parseInt(bufferedReader.readLine());
            } catch (IOException e) {
                write("Это не число");
            }
            if (value < 100) {
                currentPlatform = value;
                write("Выбрана пирамида №" + currentPlatform);
            }
        }

        while (port.openPort()) {
            String strValue = null;
            try {
                assert bufferedReader != null;
                strValue = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            long value = Long.parseLong(strValue);
            if (value == 999) {
                write(platform2product.toString());
                break;
            }
            if (value < 100) {
                currentPlatform = value;
                write("Выбрана пирамида №" + currentPlatform);
            } else {
                if (platform2product.containsKey(currentPlatform)) {
                    platform2product.get(currentPlatform).add(strValue);

                } else {
                    Set<String> set = new TreeSet<>(Comparator.naturalOrder());
                    set.add(strValue);
                    platform2product.put(currentPlatform, set);
                }
                write(strValue+": "+currentPlatform);
            }
        }
        port.closePort();
    }
}