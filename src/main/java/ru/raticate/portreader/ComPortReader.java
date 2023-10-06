package ru.raticate.portreader;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ComPortReader {

    private final Logger logger;
    private final long date;
    private final JdbcTemplate jdbcTemplate;

    ComPortReader(JdbcTemplate jdbcTemplate, Logger logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.logger = logger;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        Duration duration = Duration.between(then, now);
        date = duration.toDays() + 2;
    }


    public void sendQuery(Map<Integer, Set<Integer>> platform2product) {

        Map<Integer, Integer> idListHaff2idDog = new TreeMap<>();
        StringBuilder updateStateOfDog = new StringBuilder("update DOGOVOR set SOSDOG = 4 where IDDOGOVOR in (");
        StringJoiner listHaffIds = new StringJoiner(", ");

        platform2product.forEach((key, value) -> {
            for (Integer integer : value) {
                listHaffIds.add(integer.toString());
            }
        });
        logger.log("select a.IDLISTHAFF, c.IDDOGOVOR from LISTHAFF a join LISTIZD b on a.idizd = b.id join DOGOVOR c on b.iddog = c.iddogovor where a.IDLISTHAFF in (" + listHaffIds + ")");
        jdbcTemplate.query(
                "select a.IDLISTHAFF, c.IDDOGOVOR from LISTHAFF a join LISTIZD b on a.idizd = b.id join DOGOVOR c on b.iddog = c.iddogovor where a.IDLISTHAFF in (" + listHaffIds + ")", rs -> {
                    idListHaff2idDog.put(rs.getInt(1), rs.getInt(2));
                });


        StringJoiner dogovorIds = new StringJoiner(", ");

        platform2product.forEach((key, value) -> {
            for (Integer integer : value) {
                try {

                    dogovorIds.add(idListHaff2idDog.get(integer).toString());

                } catch (Exception e) {
                    logger.log(e.getMessage());
                }
            }
        });

        updateStateOfDog.append(dogovorIds);
        updateStateOfDog.append(");");
        logger.log(String.valueOf(updateStateOfDog));
        jdbcTemplate.execute(String.valueOf(updateStateOfDog));

        String secondQuery = "update LISTHAFF set DOTINSKLPOV = " + date + " where IDLISTHAFF in (" + listHaffIds + ");";
        logger.log(secondQuery);
        jdbcTemplate.execute(secondQuery);


        AtomicReference<StringBuilder> fourQuery = new AtomicReference<>(new StringBuilder());

        AtomicReference<StringJoiner> joiner2 = new AtomicReference<>(new StringJoiner(", "));
        platform2product.keySet().forEach(key -> {
            fourQuery.set(new StringBuilder());
            joiner2.set(new StringJoiner(", "));
            for (Integer id : platform2product.get(key)) {
                joiner2.get().add(id.toString());
            }

            fourQuery.get().append("update LISTHAFF set ncar = ").append(key).append(" where IDLISTHAFF in (").append(joiner2).append(");");
            logger.log(String.valueOf(fourQuery));
            jdbcTemplate.execute(String.valueOf(fourQuery));

        });


        platform2product.values().forEach(e -> e.forEach(s -> {
            try {
                logger.log("insert into TBL_OPER(IDUSER,IDDOG,DT,OPER) values (0, ".concat(String.valueOf(idListHaff2idDog.get(s))).concat(", ").concat(String.valueOf(date)).concat(", 9);"));
                jdbcTemplate.execute("insert into TBL_OPER(IDUSER,IDDOG,DT,OPER) values (0, ".concat(String.valueOf(idListHaff2idDog.get(s))).concat(", ").concat(String.valueOf(date)).concat(", 9);"));
            } catch (Exception ex) {
                logger.log(ex.getMessage());
            }
        }));


    }

    public Map<Integer, Set<Integer>> read(String com) {
        Map<Integer, Set<Integer>> platform2product = new TreeMap<>(Comparator.naturalOrder());

        List<SerialPort> ports = new ArrayList<>(Arrays.stream(SerialPort.getCommPorts()).toList());

        if (ports.size() == 0) {
            logger.log("No COM ports available.");
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
            System.out.println("Отсканируйте пирамиду");

            bufferedReader = new BufferedReader(new InputStreamReader(port.getInputStream()));

            try {
                value = bufferedReader.readLine();
                if (value.length() != 9) {
                    throw new Exception();
                }
                numValue = Integer.parseInt(value);
            } catch (Exception e) {
                System.out.println("Это не 9-тизначное число");
            }

            if (100000000 < numValue && numValue < 100000100) {
                currentPlatform = numValue % 100;
                System.out.println("Выбрана пирамида №" + currentPlatform);
                logger.log("Выбрана пирамида №" + currentPlatform);
            }
        }
        while (port.openPort()) {
            value = "999";
            try {
                assert bufferedReader != null;
                value = bufferedReader.readLine();

            } catch (IOException e) {
                logger.log("Ошибка чтения файла");
            }
            if (value.equals("999")) {
                logger.log("Пирамида -> изделие: " + platform2product.toString());
                AtomicInteger sum = new AtomicInteger();
                platform2product.values().forEach(e->e.forEach(o->{
                    sum.getAndIncrement();
                }));
                System.out.println("Отсканированно: " + sum.get() + " изделий.");
                break;
            }
            if (value.length() != 9) {
                System.out.println("Неверный штрих :" + value);
                continue;
            }
            try {

                numValue = Integer.parseInt(value);

            } catch (Exception e) {
                logger.log("code error");
            }

            if (100000000 < numValue && numValue < 100000100) {
                currentPlatform = numValue % 100;
                logger.log("Выбрана пирамида №" + currentPlatform);
                System.out.println("Выбрана пирамида №" + currentPlatform);
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
                logger.log(value + ": " + currentPlatform);
                System.out.println(value + ": " + currentPlatform);
            }

        }
        port.closePort();
        return platform2product;
    }
}