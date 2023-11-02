package ru.raticate.portreader.Controllers;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.raticate.portreader.DBConnection.DBWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.Reader;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class ScanController {
    public Integer count = 0;
    public Double area = 0.0;
    final
    Reader reader;

    final
    Logger logger;
    final
    JdbcTemplate jdbcTemplate;
    final
    DBWriter databaseWriter;
    final
    String production;

    @Autowired
    public ScanController(JdbcTemplate jdbcTemplate, Logger logger, DBWriter databaseWriter, Reader reader, @Qualifier("production") String production) {
        this.jdbcTemplate = jdbcTemplate;
        this.reader = reader;
        this.logger = logger;
        this.databaseWriter = databaseWriter;
        this.production = production;
    }

    @PostMapping("/start")
    Boolean start() {
        if (production.equalsIgnoreCase("sp")) {
            while (!reader.isEnd) {
                databaseWriter.sendQuery(reader.getCurrentPlatformAndProduct());
            }
            return true;
        } else if (production.equalsIgnoreCase("main")) {
            Map<Integer, Set<Integer>> platform2product = reader.startRead();
            if (!platform2product.isEmpty())
                databaseWriter.sendQuery(platform2product);
            return true;
        }
        return false;
    }

    @GetMapping("/start")
    Boolean isStart() {
        return reader.isRead;
    }



    @GetMapping("/table")
    List<MyDataRow> table() {
        List<MyDataRow> myData = null;

        if (reader.currPlatform != null) {
//            -- Первое
//            select idizd from zmatlist where TRUCK=106 and DTPIR> (45230.660);
//
//            -- Второе
//            select a.subdog,a.client,dotstart,finishdot,b.name,b.text1,b.proemh,b.proemw,b.ncam,b.sm
//            from dogovor a, listizd b where a.iddogovor=b.iddog and b.id=655227;

            myData = jdbcTemplate.query("select * from V0859_1_c1(?) order by mark", new DataClassRowMapper<>(MyDataRow.class), reader.currPlatform);
        }
        if (myData != null) {
            count = myData.size();
            area = myData.stream().mapToDouble(MyDataRow::SM).sum();
        }
        return myData;
    }

    @GetMapping("/count")
    public Integer count() {
        return count;
    }

    @GetMapping("/area")
    public String area() {
        return String.format("%.2f", area).concat("");
    }

    @GetMapping("/platform")
    public Integer platform() {
        return reader.currPlatform;
    }

    @PostMapping(value = "/api/sp_data", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean postData(@RequestBody Data data) {
        reader.isRead = true;
        reader.currPlatform = data.getPlatform();
        if (data.getProduct() != null)
            databaseWriter.sendQuery(new Pair<>(data.getPlatform(), data.getProduct()));
        return true;
    }
    @PostMapping("/send")
    Boolean send(){
        jdbcTemplate.update("");
        return true;
    }

}
