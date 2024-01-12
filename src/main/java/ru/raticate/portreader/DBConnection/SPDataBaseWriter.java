package ru.raticate.portreader.DBConnection;

import javafx.util.Pair;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.raticate.portreader.DateConvertor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;


public class SPDataBaseWriter extends DBWriter {
    DateConvertor convertor = new DateConvertor();
    record Platform(Integer num, Integer sosPir) {
    }


    public SPDataBaseWriter(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void sendQuery(Map<Integer, Set<Integer>> platform2product) {
    }

    public void sendQuery(Pair<Integer, Integer> platformAndProduct) {
        Integer platformId = platformAndProduct.getKey();
        Integer product = platformAndProduct.getValue();
        Platform platform;
        if (platformId == null) {
            return;
        }
        try {
            platform = jdbcTemplate.queryForObject("SELECT * FROM TPIR WHERE NUM = ?", new DataClassRowMapper<>(Platform.class), platformId);
        } catch (EmptyResultDataAccessException ex) {
            jdbcTemplate.update("insert into TPIR(NUM,SOSPIR) values (?,1)", platformId);
            jdbcTemplate.update("insert into tpirlist(NUM,DT,CLIENT) values (?,?,970)", platformId, convertor.dateToDouble(LocalDateTime.now()));
            platform = jdbcTemplate.queryForObject("SELECT * FROM TPIR WHERE NUM = ?", new DataClassRowMapper<>(Platform.class), platformId);
            }
        if (platform != null && platform.sosPir != 1) {
            jdbcTemplate.update("update TPIR set sospir = 1 where NUM = ?", platform.num);
            jdbcTemplate.update("insert into tpirlist(NUM,DT,CLIENT) values (?,?,970)", platform.num, convertor.dateToDouble(LocalDateTime.now()));
        }
        if(product == null){
            return;
        }
        if (product == 99999995) {
            jdbcTemplate.update("insert into tpirlist(NUM,DT,CLIENT) values (?,?,970)", platformId, convertor.dateToDouble(LocalDateTime.now()));
            jdbcTemplate.update("update tpir set sospir=1 where num=?", platformId);
        } else if (product == 99999996) {
            jdbcTemplate.update("execute procedure v0382_6(?)", platformId);

        } else {
            Integer barcode = null;
            try {
                barcode = jdbcTemplate.queryForObject("select BARCODE from ZMATLIST WHERE BARCODE = ?", Integer.class, product);
            } catch (Exception ex) {
                System.out.println("Штрих кода " + barcode + " нет в ZMATLIST.");
            }
            System.out.println("Штрих из таблицы: "+barcode);
            if (barcode == null) {
                try {
                    barcode = jdbcTemplate.queryForObject("select ZMATLIST.barcode as barcode from listizd, zmatlist  where LISTIZD.id=ZMATLIST.idizd and ZMATLIST.maked is null and LISTIZD.np=?", Integer.class, product);
                    System.out.println("Штрих из таблицы: "+barcode);
                } catch (Exception ex) {
                    System.out.println("Штрих кода " + barcode + " нет в ZMATLIST.");
                }
            }
            jdbcTemplate.update("update ZMATLIST set TRUCK = ?,DTPIR = ? where BARCODE = ?", platformId, convertor.dateToDouble(LocalDateTime.now()), barcode);
        }
    }
}

