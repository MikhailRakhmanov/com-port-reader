package ru.raticate.portreader.Controllers.DTO;

public class PairDTO {
    private Integer platform,product;

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    PairDTO(Integer platform, Integer product) {
        this.platform = platform;
        this.product = product;
    }
}
