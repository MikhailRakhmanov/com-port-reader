package ru.raticate.portreader.Controllers;

public class Data {
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

    Data(Integer platform, Integer product) {
        this.platform = platform;
        this.product = product;
    }
}
