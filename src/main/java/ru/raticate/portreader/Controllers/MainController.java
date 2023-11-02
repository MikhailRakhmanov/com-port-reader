package ru.raticate.portreader.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.raticate.portreader.DateConvertor;
import ru.raticate.portreader.Loggers.Logger;
import ru.raticate.portreader.Reader.Reader;

import java.util.List;

@Controller
public class MainController {
    final Reader reader;
    final
    String barcode;
    final
    Logger logger;
    final JdbcTemplate jdbcTemplate;
   final ScanController scanController;

    @Autowired
    public MainController(Reader reader, Logger logger, @Qualifier("exitBarcode") String barcode, JdbcTemplate jdbcTemplate, ScanController scanController) {
        this.reader = reader;
        this.logger = logger;
        this.barcode = barcode;
        this.jdbcTemplate = jdbcTemplate;
        this.scanController = scanController;
    }

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute("barcode", barcode);
        return "index";
    }

    @GetMapping("/scan")
    String scan() {
        return "scan_page";
    }

    @GetMapping("/print")
    String print(Model model) {
        model.addAttribute("platform", reader.currPlatform);
        model.addAttribute("data", scanController.table());
        model.addAttribute("area", scanController.area);
        model.addAttribute("count", scanController.count);
        model.addAttribute("convertor",new DateConvertor());
        return "print";
    }

}