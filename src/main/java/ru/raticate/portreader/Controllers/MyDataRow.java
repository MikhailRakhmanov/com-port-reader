package ru.raticate.portreader.Controllers;

record MyDataRow(String CAPTION, String MARK, Integer NUMDOG, String RAZ, String CLIENT, Double SM, Integer DTS,
                 Integer DTF) {
    @Override
    public String CAPTION() {
        return CAPTION;
    }

    @Override
    public String MARK() {
        return MARK;
    }

    @Override
    public Integer NUMDOG() {
        return NUMDOG;
    }

    @Override
    public String RAZ() {
        return RAZ;
    }

    @Override
    public String CLIENT() {
        return CLIENT;
    }

    @Override
    public Double SM() {
        if (SM != null) {
            return SM;
        } else return 0.0;
    }

    @Override
    public Integer DTS() {
        return DTS;
    }

    @Override
    public Integer DTF() {
        return DTF;
    }
}
