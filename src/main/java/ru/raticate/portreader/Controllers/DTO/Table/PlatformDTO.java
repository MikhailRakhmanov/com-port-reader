package ru.raticate.portreader.Controllers.DTO.Table;

import org.springframework.stereotype.Component;

import java.util.List;

public record PlatformDTO(List<Product> products, Integer platformName, Integer count, Double area) {}
