package main.java.wordAnalysys;

import main.java.wordAnalysys.variantsAnalysys.ObjectsConformity;

public class Analyzer {
    private ObjectsConformity Brand = new ObjectsConformity(main.java.sqlObjects.meta.Brand.class);

    public String percentageWithDot(Integer value) {
        return Integer.toString(value / 100) + "." + value % 100;
    }



}
