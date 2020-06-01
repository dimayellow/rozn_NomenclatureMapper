package main.java.common.obj.sqlObjects;

import java.util.ArrayList;
import java.util.Objects;

public class ForecastParseNom {

    private final int brand;
    private final int unit;
    private final int catalog;
    private final int soda;
    private final int tara;
    private final int temperature;
    private final int grade;

    private final ArrayList<String> tailList = new ArrayList<String>();
    private final String countUnitName;
    private final String tovarName;

    private final int forecastTovarId;


    public static class Builder {
        private final String title;

        private int brandId = 0;
        private int unit = 0;
        private int catalog = 0;
        private int soda = 0;
        private int tara = 0;
        private int temperature = 0;
        private int grade = 0;
        private String countUnitName = "";
        private int forecastTovarId = -1;

        private String tailList = "";

        public Builder(String title) {
            this.title = title;
        }

        public Builder setForecastTovarId(int forecastTovarId) {
            this.forecastTovarId = forecastTovarId;
            return this;
        }

        public Builder setBrandId(int brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder setUnit(int unit) {
            this.unit = unit;
            return this;
        }

        public Builder setCatalog(int catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder setSoda(int soda) {
            this.soda = soda;
            return this;
        }

        public Builder setTara(int tara) {
            this.tara = tara;
            return this;
        }

        public Builder setTemperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder setGrade(int grade) {
            this.grade = grade;
            return this;
        }

        public Builder setCountUnitName(String countUnitName) {
            this.countUnitName = countUnitName;
            return this;
        }

        public Builder setTailList(String tailList) {
            this.tailList = tailList;
            return this;
        }

        public ForecastParseNom build() {
            return new ForecastParseNom(this);
        }
    }

    public ForecastParseNom(Builder builder) {
        brand  = builder.brandId;
        unit = builder.unit;
        catalog = builder.catalog;
        soda = builder.soda;
        tara = builder.tara;
        temperature = builder.temperature;
        grade = builder.grade;
        countUnitName = builder.countUnitName;
        forecastTovarId = builder.forecastTovarId;

        tovarName = builder.title;


        for (String s : builder.tailList.split(" ")) {
            tailList.add(s);
        }
    }

    public int getBrand() {
        return brand;
    }

    public int getUnit() {
        return unit;
    }

    public int getCatalog() {
        return catalog;
    }

    public int getSoda() {
        return soda;
    }

    public int getTara() {
        return tara;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getGrade() {
        return grade;
    }

    public ArrayList<String> getTailList() {
        return tailList;
    }

    public String getCountUnitName() {
        return countUnitName;
    }

    public int getForecastTovarId() {
        return forecastTovarId;
    }

    public String getTovarName() {
        return tovarName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForecastParseNom)) return false;
        ForecastParseNom that = (ForecastParseNom) o;
        return brand == that.brand &&
                unit == that.unit &&
                catalog == that.catalog &&
                soda == that.soda &&
                tara == that.tara &&
                temperature == that.temperature &&
                grade == that.grade &&
               //tailList.equals(that.tailList) &&
                countUnitName.equals(that.countUnitName);
    }

    @Override
    public String toString() {
        return "ForecastParseNom{" +
                "brand=" + brand +
                ", unit=" + unit +
                ", catalog=" + catalog +
                ", soda=" + soda +
                ", tara=" + tara +
                ", temperature=" + temperature +
                ", grade=" + grade +
                ", tailList=" + tailList +
                ", countUnitName='" + countUnitName + '\'' +
                ", forecastTovarId=" + forecastTovarId +
                ", tovarName='" + tovarName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, unit, catalog, soda, tara, temperature, grade);
    }
}
