package ru.raticate.portreader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {


    @Value("${com}")
    String com;
    @Bean
    ComPortReader comPortReader(){
        ComPortReader comPortReader = new ComPortReader();
        comPortReader.read(com);
        return comPortReader;
    }
}
