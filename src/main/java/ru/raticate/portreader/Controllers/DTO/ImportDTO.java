package ru.raticate.portreader.Controllers.DTO;


import ru.raticate.portreader.DateConvertor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ImportDTO {
    Integer IDZMAT;
    Integer DOTPRN;
    String PACKET;
    Integer NUM;
    String NOTES;
    String NOTES1;
    String FNAME;
    Character SLC;
    Integer WDATE;
    LocalDateTime DTP;
    LocalDateTime DOT6;
    Integer IDBRIG;
    Integer PARENT;
    Integer MANAGER;
    Integer VD;
    Integer KOLVO;

    public Integer getIDZMAT() {
        return IDZMAT;
    }

    public void setIDZMAT(Integer IDZMAT) {
        this.IDZMAT = IDZMAT;
    }

    public Integer getDOTPRN() {
        return DOTPRN;
    }

    public void setDOTPRN(Integer DOTPRN) {
        this.DOTPRN = DOTPRN;
    }

    public String getPACKET() {
        return PACKET;
    }

    public void setPACKET(String PACKET) {
        this.PACKET = PACKET;
    }

    public Integer getNUM() {
        return NUM;
    }

    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }

    public String getNOTES() {
        return NOTES;
    }

    public void setNOTES(String NOTES) {
        this.NOTES = NOTES;
    }

    public String getNOTES1() {
        return NOTES1;
    }

    public void setNOTES1(String NOTES1) {
        this.NOTES1 = NOTES1;
    }

    public String getFNAME() {
        return FNAME;
    }

    public void setFNAME(String FNAME) {
        this.FNAME = FNAME;
    }

    public Character getSLC() {
        return SLC;
    }

    public void setSLC(Character SLC) {
        this.SLC = SLC;
    }

    public Integer getWDATE() {
        return WDATE;
    }

    public void setWDATE(Integer WDATE) {
        this.WDATE = WDATE;
    }

    public LocalDate getDTP() {
        return DTP.toLocalDate();
    }

    public void setDTP(Integer DTP) {
        this.DTP = dateConvertor.doubleToDate(DTP);
    }


    public LocalDate getDOT6() {
        return DOT6.toLocalDate();
    }

    public void setDOT6(Integer DOT6) {
        this.DOT6 = dateConvertor.doubleToDate(DOT6);
    }

    public Integer getIDBRIG() {
        return IDBRIG;
    }

    public void setIDBRIG(Integer IDBRIG) {
        this.IDBRIG = IDBRIG;
    }

    public Integer getPARENT() {
        return PARENT;
    }

    public void setPARENT(Integer PARENT) {
        this.PARENT = PARENT;
    }

    public Integer getMANAGER() {
        return MANAGER;
    }

    public void setMANAGER(Integer MANAGER) {
        this.MANAGER = MANAGER;
    }

    public Integer getVD() {
        return VD;
    }

    public void setVD(Integer VD) {
        this.VD = VD;
    }

    public Integer getKOLVO() {
        return KOLVO;
    }

    public void setKOLVO(Integer KOLVO) {
        this.KOLVO = KOLVO;
    }

    private static final DateConvertor  dateConvertor = new DateConvertor();

    public ImportDTO(Integer IDZMAT, Integer DOTPRN, String PACKET, Integer NUM, String NOTES, String NOTES1, String FNAME, Character SLC, Integer WDATE, Integer DTP, Integer DOT6, Integer IDBRIG, Integer PARENT, Integer MANAGER, Integer VD, Integer KOLVO) {

        this.IDZMAT = IDZMAT;
        this.DOTPRN = DOTPRN;
        this.PACKET = PACKET;
        this.NUM = NUM;
        this.NOTES = NOTES;
        this.NOTES1 = NOTES1;
        this.FNAME = FNAME;
        this.SLC = SLC;
        this.WDATE = WDATE;
        if (DTP != null) {
            this.DTP = dateConvertor.doubleToDate(DTP);
        }
        if (DOT6 != null) {
            this.DOT6 = dateConvertor.doubleToDate(DOT6);
        }
        this.IDBRIG = IDBRIG;
        this.PARENT = PARENT;
        this.MANAGER = MANAGER;
        this.VD = VD;
        this.KOLVO = KOLVO;
    }
}
