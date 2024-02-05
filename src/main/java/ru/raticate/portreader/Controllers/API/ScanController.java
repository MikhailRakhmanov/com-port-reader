package ru.raticate.portreader.Controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raticate.portreader.Controllers.DTO.Table.PlatformDTO;
import ru.raticate.portreader.Controllers.DTO.Table.Product;

import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequestMapping("/scan")
public class ScanController {

    final
    JdbcTemplate jdbcTemplate;
    ApiController apiController;


    @Autowired
    public ScanController(@Qualifier("sPJdbcTemplate") JdbcTemplate jdbcTemplate, ApiController apiController) {
        this.jdbcTemplate = jdbcTemplate;
        this.apiController = apiController;
    }


    @GetMapping("/start")
    Boolean isStart() {
        return true;
    }

    @GetMapping("/table")
    public PlatformDTO platform() {
        List<Product> products = null;
        int count = 0;
        double area = 0.0;
        if (apiController.currentPlatform != null) {
            products = jdbcTemplate.query("select * from V0859_1_c1(?) order by mark", new DataClassRowMapper<>(Product.class), apiController.currentPlatform);
            count = products.size();
            area = products.stream().mapToDouble(Product::getSm).sum();
        }
        DecimalFormat df = new DecimalFormat("#.###");
        return new PlatformDTO(products, apiController.currentPlatform, count, df.format(area));
    }


}
