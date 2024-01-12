package ru.raticate.portreader.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.raticate.portreader.Controllers.API.ApiController;
import ru.raticate.portreader.Controllers.API.ScanController;
import ru.raticate.portreader.Controllers.DTO.Table.PlatformDTO;
import ru.raticate.portreader.DateConvertor;

@Controller()
public class PrintController {
    final
    ApiController apiController;
    final PlatformDTO platform;


    public PrintController(ApiController apiController, ScanController scanController) {
        this.apiController = apiController;
        this.platform = scanController.platform();

    }

    @GetMapping("/print")
    String print(Model model) {
        model.addAttribute("platform", apiController.currentPlatform);

        model.addAttribute("data", platform.products());
        model.addAttribute("area", platform.area());
        model.addAttribute("count", platform.count());
        model.addAttribute("convertor", new DateConvertor());
        return "print";
    }
}
