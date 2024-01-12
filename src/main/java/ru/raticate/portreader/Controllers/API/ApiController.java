package ru.raticate.portreader.Controllers.API;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.raticate.portreader.Controllers.DTO.PairDTO;
import ru.raticate.portreader.Controllers.DTO.DeliveyDTO;
import ru.raticate.portreader.Controllers.DTO.ImportDTO;
import ru.raticate.portreader.Controllers.DTO.Table.PlatformDTO;
import ru.raticate.portreader.Controllers.DTO.Table.Product;
import ru.raticate.portreader.DBConnection.DBWriter;
import ru.raticate.portreader.Servises.TPIR;

import java.util.List;

import static org.springframework.jdbc.core.JdbcOperationsExtensionsKt.query;

@RestController
@RequestMapping("/api")
public class ApiController {
    final
    DBWriter databaseWriter;
    final
    JdbcTemplate sPJdbcTemplate;
    final
    JdbcTemplate mainJdbcTemplate;
    public Integer currentPlatform;

    public ApiController(DBWriter databaseWriter,
                         @Qualifier("sPJdbcTemplate")
                         JdbcTemplate sPJdbcTemplate,
                         @Qualifier("mainJdbcTemplate")
                         JdbcTemplate mainJdbcTemplate) {
        this.databaseWriter = databaseWriter;
        this.sPJdbcTemplate = sPJdbcTemplate;
        this.mainJdbcTemplate = mainJdbcTemplate;
    }

    @PostMapping(value = "/sp_data", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean postData(@RequestBody PairDTO pair) {

        currentPlatform = pair.getPlatform() == null ? currentPlatform : pair.getPlatform();
        if (pair.getProduct() != null) {
            databaseWriter.sendQuery(new Pair<>(pair.getPlatform(), pair.getProduct()));
        }
        return true;
    }

    @GetMapping("/import")
    public List<ImportDTO> importOrders() {
        return mainJdbcTemplate.query("select IDZMAT,DOTPRN,PACKET,NUM,NOTES,NOTES1,FNAME,SLC,WDATE,DTP,DOT6,IDBRIG,PARENT,MANAGER,VD,KOLVO from V0172_3_C1(15,16267820,0) where DOTPRN is null order by idbrig,wdate",
                new DataClassRowMapper<>(ImportDTO.class));
    }

    @GetMapping("/delivery")
    private List<DeliveyDTO> delivery() {
        return sPJdbcTemplate.query("select * from V0852 order by DOTOUT,TIMEOUT,NUMPIR", new DataClassRowMapper<>(DeliveyDTO.class));
    }

    @GetMapping("/tpir")
    public List<TPIR> platformList() {
        return sPJdbcTemplate.query("select * from TPIR order by num", new DataClassRowMapper<>(TPIR.class));
    }
    @GetMapping("/table/{id}")
    public PlatformDTO platform(@PathVariable Integer id) {
        List<Product> products = sPJdbcTemplate.query("select * from V0859_1_c1(?) order by mark", new DataClassRowMapper<>(Product.class), id);
        int count = 0;
        double area = 0.0;
        if (products != null) {
            count = products.size();
            area = products.stream().mapToDouble(Product::getSm).sum();
        }
        System.out.println(products);

        return new PlatformDTO(products, currentPlatform, count,area);
    }
}
