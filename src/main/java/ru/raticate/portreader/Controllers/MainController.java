package ru.raticate.portreader.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.raticate.portreader.Loggers.Logger;

@Controller
public class MainController {
    final
    String barcode;
    final
    Logger logger;

    public MainController(Logger logger, String barcode) {
        this.logger = logger;
        this.barcode = barcode;
    }

    @GetMapping("/")
    String index(Model model) {

        model.addAttribute("data", logger.data);
        model.addAttribute("barcode", barcode);
        return "index.html";
    }
}