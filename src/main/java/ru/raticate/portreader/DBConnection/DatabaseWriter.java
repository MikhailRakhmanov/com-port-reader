package ru.raticate.portreader.DBConnection;

import javafx.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DateConvertor;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Loggers.LoggerLevel;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseWriter extends DBWriter {


    public DatabaseWriter(JdbcTemplate jdbcTemplate, Logger logger) {
        super(jdbcTemplate, logger);
    }

    public void sendQuery(Map<Integer, Set<Integer>> platform2product) {
        DateConvertor convertor = new DateConvertor();

        Map<Integer, Integer> idListHaff2idDog = new TreeMap<>();
        StringBuilder updateStateOfDog = new StringBuilder("update DOGOVOR set SOSDOG = 4 where IDDOGOVOR in (");
        StringJoiner listHaffIds = new StringJoiner(", ");

        platform2product.forEach((key, value) -> {
            for (Integer integer : value) {
                listHaffIds.add(integer.toString());
            }
        });
        logger.log("select a.IDLISTHAFF, c.IDDOGOVOR from LISTHAFF a join LISTIZD b on a.idizd = b.id join DOGOVOR c on b.iddog = c.iddogovor where a.IDLISTHAFF in (" + listHaffIds + ")", LoggerLevel.File);
        jdbcTemplate.query(
                "select a.IDLISTHAFF, c.IDDOGOVOR from LISTHAFF a join LISTIZD b on a.idizd = b.id join DOGOVOR c on b.iddog = c.iddogovor where a.IDLISTHAFF in (" + listHaffIds + ")", rs -> {
                    idListHaff2idDog.put(rs.getInt(1), rs.getInt(2));
                });
        logger.log(idListHaff2idDog.toString(), LoggerLevel.File);
        StringJoiner dogovorIds = new StringJoiner(", ");
        platform2product.forEach((key, value) -> {
            for (Integer integer : value) {
                try {
                    Integer idDog = idListHaff2idDog.get(integer);
                    if (idDog != null)
                        dogovorIds.add(idDog.toString());

                } catch (Exception e) {
                    logger.log(e.getMessage(), LoggerLevel.File);
                }
            }
        });

        String secondQuery = "update LISTHAFF set DOTINSKLPOV = " + convertor.dateToDouble(LocalDateTime.now()) + " where IDLISTHAFF in (" + listHaffIds + ");";
        logger.log(secondQuery, LoggerLevel.File);
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
            logger.log(String.valueOf(fourQuery), LoggerLevel.File);
            jdbcTemplate.execute(String.valueOf(fourQuery));

        });


        Set<Integer> allChangedDogs = new HashSet<>();
        Set<Integer> dogovorsWithNoChangeSOSDOG = new HashSet<>();
        if (dogovorIds.length() != 0)
            jdbcTemplate.query("select distinct c.IDDOGOVOR, a.ncar from LISTHAFF a join LISTIZD b on a.idizd = b.id join DOGOVOR c on b.iddog = c.iddogovor where c.IDDOGOVOR in (" + dogovorIds + ") AND (NCAR is not NULL and  NCAR <> 999)",
                    rs -> {
                        dogovorsWithNoChangeSOSDOG.add(rs.getInt(1));
                    });
        logger.log("Договоры, где не проставлены все пирамиды: \n" + dogovorsWithNoChangeSOSDOG, LoggerLevel.File);
        StringJoiner resultDogovorIds = new StringJoiner(", ");
        for (Integer dogId : idListHaff2idDog.values()) {
            allChangedDogs.add(dogId);
            if (!dogovorsWithNoChangeSOSDOG.contains(dogId)) {
                resultDogovorIds.add(dogId.toString());
            }
        }

        updateStateOfDog.append(resultDogovorIds);
        updateStateOfDog.append(");");
        if (resultDogovorIds.length() != 0) {
            logger.log(String.valueOf(updateStateOfDog), LoggerLevel.File);
            jdbcTemplate.execute(String.valueOf(updateStateOfDog));
        }

        allChangedDogs.forEach(s -> {
            try {
                String tabel = "insert into TBL_OPER(IDUSER,IDDOG,DT,OPER) values (0, ".concat(s.toString()).concat(", ").concat(String.valueOf(convertor.dateToDouble(LocalDateTime.now()))).concat(", 9);");
                logger.log(tabel, LoggerLevel.File);
                jdbcTemplate.execute(tabel);
            } catch (Exception ex) {
                logger.log(ex.getMessage(), LoggerLevel.File);
            }
        });
        logger.log("", LoggerLevel.File);

    }

    @Override
    public void sendQuery(Pair<Integer, Integer> platformAndProduct) {
    }
}
