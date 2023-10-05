package ru.raticate.portreader;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ComPortReader {
    File file = new File("history.txt");
    FileWriter fileWriter;
    long date;

    ComPortReader() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0,0,0);
        Duration duration = Duration.between(then, now);
        date = duration.toDays();
        System.out.println(date);
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(now.toString() + '\n');
        } catch (IOException e) {
            System.err.println("Проблема записи файла истории");
        }

        if (!file.exists()) {
            try {
                assert true:
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Проблема создания файла истории");
            }
        }
    }

    public void write(String content) {
        try {
            System.out.println(content);
            fileWriter.write(content + '\n');
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Проблема записи файла истории");
        }


    }



    public void sendQuery(Map<Integer, Set<Integer>> platform2product) {
        StringBuilder firstQuery = new StringBuilder("update DOGOVOR set SOSDOG = 4 where IDDOGOVOR in [");
        StringJoiner joiner = new StringJoiner(", ");
        platform2product.forEach((key, value) -> {
            for (Integer integer : value) {
                joiner.add(integer.toString());
            }
        });
        firstQuery.append(joiner);
        firstQuery.append("];");
        System.out.println(firstQuery);

        String secondQuery = "update LISTHAFF set DOTINSKLPOV = " + date + " where IDLISTHAFF in [" + joiner +
                "];";
        System.out.println(secondQuery);

        StringBuilder fourQuery = new StringBuilder();

        AtomicReference<StringJoiner> joiner2 = new AtomicReference<>(new StringJoiner(", "));
        platform2product.keySet().forEach(key -> {
            joiner2.set(new StringJoiner(", "));
            for (Integer id: platform2product.get(key)){
                joiner2.get().add(id.toString());
            }
            fourQuery.append("update LISTHAFF set ncar = ").append(key).append(" where IDLISTHAFF in [").append(joiner2).append("];\n");
        });
        System.out.print(fourQuery);


        String journalQuery ="insert into TBL_OPER(IDUSER,IDDOG,DT,OPER) values ";
        StringJoiner joiner3 = new StringJoiner(", ");
        platform2product.values().forEach(e->e.forEach(s-> joiner3.add("(0, ".concat(String.valueOf(s)).concat(", ").concat(String.valueOf(date)).concat(", 9)"))));
        journalQuery= journalQuery.concat(joiner3.toString()).concat(";");
        System.out.println(journalQuery);
    }

    public Map<Integer, Set<Integer>> read(String com) {
        Map<Integer, Set<Integer>> platform2product = new TreeMap<>(Comparator.naturalOrder());

        List<SerialPort> ports = new java.util.ArrayList<>(Arrays.stream(SerialPort.getCommPorts()).toList());

        if (ports.size() == 0) {
            write("No COM ports available.");
            return platform2product;
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
        int currentPlatform = -1;
        String value;
        int numValue = 0;
        while (currentPlatform == -1 && port.openPort()) {
            write("Отсканируйте пирамиду");

            bufferedReader = new BufferedReader(new InputStreamReader(port.getInputStream()));

            try {
                value = bufferedReader.readLine();
                if (value.length() != 9) {
                    throw new Exception();
                }
                numValue = Integer.parseInt(value);
            } catch (Exception e) {
                write("Это не 9-тизначное число");
            }

            if (100000000 < numValue && numValue < 100000100) {
                currentPlatform = numValue % 100;
                write("Выбрана пирамида №" + currentPlatform);
            }
        }
        while (port.openPort()) {

            try {
                assert bufferedReader != null;
                value = bufferedReader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (value.equals("999")) {
                write(platform2product.toString());
                break;
            }
            if (value.length() != 9) {
                System.out.println("Неверный штрих :" + value);
                continue;
            }
            try {

                numValue = Integer.parseInt(value);

            } catch (Exception e) {
                write("code error");
            }

            if (100000000 < numValue && numValue < 100000100) {
                currentPlatform = numValue % 100;
                write("Выбрана пирамида №" + currentPlatform);
            } else {
                final Integer[] platformToRemove = {null};
                int num = numValue/1000;
                platform2product.forEach((key, value1) -> {
                    value1.remove(num);
                    if (value1.isEmpty()) {
                        platformToRemove[0] = key;
                    }
                });
                if (platformToRemove[0] !=null){
                    platform2product.remove(platformToRemove[0]);
                }
                if (platform2product.containsKey(currentPlatform)) {
                    platform2product.get(currentPlatform).add(numValue / 1000);
                } else {
                    Set<Integer> set = new TreeSet<>(Comparator.naturalOrder());
                    set.add(numValue / 1000);
                    platform2product.put(currentPlatform, set);
                }
                write(value + ": " + currentPlatform);
            }

        }
        port.closePort();
        return platform2product;
    }
}