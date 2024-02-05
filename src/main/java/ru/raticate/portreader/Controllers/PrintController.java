package ru.raticate.portreader.Controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.raticate.portreader.Controllers.API.ApiController;
import ru.raticate.portreader.Controllers.API.ScanController;
import ru.raticate.portreader.Controllers.DTO.Table.PlatformDTO;
import ru.raticate.portreader.Controllers.DTO.Table.Product;
import ru.raticate.portreader.DateConvertor;

import java.text.DecimalFormat;
import java.util.List;

@Controller()
public class PrintController {
    final
    ApiController apiController;
    final PlatformDTO platform;
    final
    JdbcTemplate sPJdbcTemplate;


    public PrintController(ApiController apiController, ScanController scanController,@Qualifier("sPJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.apiController = apiController;
        this.platform = scanController.platform();
        this.sPJdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/print")
    String print(Model model) {
        model.addAttribute("platform", apiController.currentPlatform);

        List<Product> products = sPJdbcTemplate.query("select * from V0859_1_c1(?) order by mark", new DataClassRowMapper<>(Product.class), apiController.currentPlatform);
        int count = 0;
        Double area = 0.0;
        count = products.size();
        area = products.stream().mapToDouble(Product::getSm).sum();
        DecimalFormat df = new DecimalFormat("#.###");

        model.addAttribute("data", products);
        model.addAttribute("area", df.format(area));
        model.addAttribute("count", count);
        model.addAttribute("convertor", new DateConvertor());
        return "print";
    }
}
