package ru.raticate.portreader.Controllers.DTO;

public record MyDataRow(String CAPTION, String MARK, Integer NUMDOG, String RAZ, String CLIENT, Double SM, Integer DTS,
                        Integer DTF) {

    @Override
    public Double SM() {
        if (SM != null) {
            return SM;
        } else return 0.0;
    }

}
