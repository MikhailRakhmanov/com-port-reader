package ru.raticate.portreader.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/download")
public class DownloadController {
    @GetMapping("")
    String index(){
        return "/download/index";
    }
}
