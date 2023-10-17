package ru.raticate.portreader.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raticate.portreader.DBConnection.DatabaseWriter;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.ComPortReader;

import java.util.List;

@RestController
public class ScanController {
    final
    ComPortReader reader;

    final
    Logger logger;
    final
    DatabaseWriter databaseWriter;

    @Autowired
    public ScanController(Logger logger, DatabaseWriter databaseWriter, ComPortReader reader) {
        this.reader = reader;
        this.logger = logger;
        this.databaseWriter = databaseWriter;
    }

    @PostMapping("/start")
    Boolean start() {
        var platform2product = reader.read();
        if (!platform2product.isEmpty())
            databaseWriter.sendQuery(platform2product);
        return true;
    }

    @GetMapping("/data")
    List<String> values() {
        return logger.data;
    }
}
