package ru.raticate.portreader.Controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raticate.portreader.Controllers.DTO.MyDataRow;
import ru.raticate.portreader.DBConnection.DBWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.Reader;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/scan")
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
            while(true){
                databaseWriter.sendQuery(reader.getCurrentPlatformAndProduct());
            }
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
        return true;
    }



    @GetMapping("/table")
    public List<MyDataRow> table() {
        List<MyDataRow> myData = null;
        if (reader.currPlatform != null) {
            myData = jdbcTemplate.query("select * from V0859_1_c1(?) order by mark", new DataClassRowMapper<>(MyDataRow.class), reader.currPlatform);
            if (myData != null) {
                count = myData.size();
                area = myData.stream().mapToDouble(MyDataRow::SM).sum();
            }
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

    @GetMapping("/cur_id")
    public Integer platform() {
        return reader.currPlatform;
    }


    @PostMapping("/send")
    Boolean send(){
        jdbcTemplate.update("");
        return true;
    }

}
