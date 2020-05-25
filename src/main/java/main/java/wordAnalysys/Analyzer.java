package main.java.wordAnalysys;

import main.java.sqlObjects.Brand;
import main.java.sqlObjects.SQLBaseObject;
import main.java.wordAnalysys.variantsAnalysys.ObjectsConformity;

import java.util.HashMap;

public class Analyzer {
    private ObjectsConformity Brand = new ObjectsConformity(main.java.sqlObjects.Brand.class);

    public String percentageWithDot(Integer value) {
        return Integer.toString(value / 100) + "." + value % 100;
    }



}
