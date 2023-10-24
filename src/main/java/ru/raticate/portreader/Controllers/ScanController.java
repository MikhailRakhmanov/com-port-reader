package ru.raticate.portreader.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raticate.portreader.DBConnection.DatabaseWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.Reader;

import java.util.Map;
import java.util.Set;

@RestController
public class ScanController {
    final
    Reader reader;

    final
    Logger logger;
    final
    DatabaseWriter databaseWriter;

    @Autowired
    public ScanController(Logger logger, DatabaseWriter databaseWriter, Reader reader) {
        this.reader = reader;
        this.logger = logger;
        this.databaseWriter = databaseWriter;
    }

    @PostMapping("/start")
    Boolean start() {
        reader.clearData();
        Map<Integer, Set<Integer>> platform2product = reader.startRead();
        if (!platform2product.isEmpty())
            databaseWriter.sendQuery(platform2product);
        return true;
    }
    @GetMapping("/start")
    Boolean isStart() {
        return reader.isRead;
    }

    @GetMapping("/data")
    Map<Integer, Set<Integer>> values() {
        return reader.getCurrentData();
    }
}
