package ru.raticate.portreader.DBConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.raticate.portreader.Loggers.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;


public class SPDataBaseWriter {

    record Platform(Integer num, Integer sosPir) {
    }


    double date;
    JdbcTemplate jdbcTemplate;
    Logger logger;

    @Autowired
    public SPDataBaseWriter(JdbcTemplate jdbcTemplate,Logger logger) {
        this.logger = logger;
        this.jdbcTemplate = jdbcTemplate;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        Duration duration = Duration.between(then, now);
        date = (double) duration.toSeconds()/86400;
        System.out.println(date);
    }

    public void sendQuery(Integer platformId, Integer product) {

        Platform platform = null;
        if (platformId == null) {
            return;
        }

        if (platformId <= 215 || platformId == 666) {
            platform = jdbcTemplate.queryForObject("SELECT * FROM TPIR WHERE NUM = ?", new DataClassRowMapper<>(Platform.class), platformId);

            if ((platform != null ? platform.sosPir : null) == null) {
                assert platform != null;
                jdbcTemplate.update("insert into TPIR(NUM,SOSPIR) values (?,1)", platformId);
                jdbcTemplate.update("insert into tpirlist(NUM,DT,CLIENT) values (?,?,970)", platform.num, date);
            } else if (platform.sosPir != 1) {
                jdbcTemplate.update("update TPIR set sospir = 1 where NUM = ?", platform.num);
                jdbcTemplate.update("insert into tpirlist(NUM,DT,CLIENT) values (?,?,970)", platform.num, date);
            }
        } else if (product == 99999994) {
//            npir = jdbcTemplate.queryForObject("select  from V0859_1_c1(?) order by mark", (rs, rowNum) -> {}, platformId);
        } else if (product == 99999995) {
        } else if (product == 99999996) {
        } else {

            Integer barcode = jdbcTemplate.queryForObject("select BARCODE from ZMATLIST WHERE BARCODE = ?", Integer.class, product);
            if (barcode == null) {
                barcode = jdbcTemplate.queryForObject("select ZMATLIST.barcode as barcode from listizd, zmatlist  where LISTIZD.id=ZMATLIST.idizd and ZMATLIST.maked is null and LISTIZD.np=?", Integer.class, product);
            }
            jdbcTemplate.update("update ZMATLIST set TRUCK = ?,DTPIR = ? where BARCODE = ?", platformId, date, product);

        }
    }

}

