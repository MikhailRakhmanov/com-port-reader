package ru.raticate.portreader.Controllers.DTO.Table;

import ru.raticate.portreader.DateConvertor;

import java.time.LocalDate;

public class Product {
    static DateConvertor dateConvertor;
    String caption;
    String mark;
    Integer numDog;
    String raz;
    String client;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getNumDog() {
        return numDog;
    }

    public void setNumDog(Integer numDog) {
        this.numDog = numDog;
    }

    public String getRaz() {
        return raz;
    }

    public void setRaz(String raz) {
        this.raz = raz;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Double getSm() {
        return sm;
    }

    public void setSm(Double sm) {
        this.sm = sm;
    }

    public LocalDate getDts() {
        return dts;
    }

    public void setDts(LocalDate dts) {
        this.dts = dts;
    }

    public LocalDate getDtf() {
        return dtf;
    }

    public void setDtf(LocalDate dtf) {
        this.dtf = dtf;
    }

    Double sm;
    LocalDate dts;
    LocalDate dtf;

    public Product(String caption, String mark, Integer numDog, String raz, String client, Double sm, Integer dts, Integer dtf) {
        dateConvertor = new DateConvertor();
        this.caption = caption;
        this.mark = mark;
        this.numDog = numDog;
        this.raz = raz;
        this.client = client;
        this.sm = sm;
        this.dts = dateConvertor.doubleToDate(dts).toLocalDate();
        this.dtf = dateConvertor.doubleToDate(dtf).toLocalDate();
    }

}
