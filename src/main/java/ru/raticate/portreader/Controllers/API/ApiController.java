package ru.raticate.portreader.Controllers.API;

import javafx.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.raticate.portreader.Controllers.DTO.Data;
import ru.raticate.portreader.DBConnection.DBWriter;
import ru.raticate.portreader.Reader.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    final
    Reader reader;
    final
    DBWriter databaseWriter;

    public ApiController(Reader reader, DBWriter databaseWriter) {
        this.reader = reader;
        this.databaseWriter = databaseWriter;
    }

    @PostMapping(value = "/sp_data", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean postData(@RequestBody Data data) {
        reader.isRead = true;
        reader.currPlatform = data.getPlatform();
        if (data.getProduct() != null)
            databaseWriter.sendQuery(new Pair<>(data.getPlatform(), data.getProduct()));
        return true;
    }
    @GetMapping("/test")
    public List<String> importOrders(){
        BufferedReader bufferedReader= null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            Runtime.getRuntime().exec("python src/main/python/test.py").getInputStream(),
                            Charset.forName("windows-1251")
                    ));
        } catch (IOException ignored) {
        }
        return bufferedReader == null? new ArrayList<String>(): bufferedReader.lines().toList();
    }
}
