package ru.raticate.portreader.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.raticate.portreader.Loggers.Logger;

@Controller
public class MainController {
    final
    Logger logger;

    public MainController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute("data", logger.data);
        return "index.html";
    }
}