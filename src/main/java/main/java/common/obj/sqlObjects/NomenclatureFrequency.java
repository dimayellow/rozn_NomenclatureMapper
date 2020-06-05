package main.java.common.obj.sqlObjects;

public class NomenclatureFrequency {
    private final ForecastParseNom forecastParseNom;
    private final int brandFrequency;
    private final int catalogFrequency;
    private final int gradeFrequency;
    private final int sodaFrequency;
    private final int taraFrequency;
    private final int temperatureFrequency;
    private final int unitFrequency;
    private final int metaWeight;
    private final int tailWeight;


    public static class Builder {
        private final ForecastParseNom forecastParseNom;
        private int brandFrequency = 0;
        private int catalogFrequency = 0;
        private int gradeFrequency = 0;
        private int sodaFrequency = 0;
        private int taraFrequency = 0;
        private int temperatureFrequency = 0;
        private int unitFrequency = 0;
        private int metaWeight = 100;
        private int tailWeight = 0;

        public Builder(ForecastParseNom forecastParseNom) {
            this.forecastParseNom = forecastParseNom;
        }

        public Builder setBrandFrequency(int brandFrequency) {
            this.brandFrequency = brandFrequency;
            return this;
        }

        public Builder setCatalogFrequency(int catalogFrequency) {
            this.catalogFrequency = catalogFrequency;
            return this;
        }

        public Builder setGradeFrequency(int gradeFrequency) {
            this.gradeFrequency = gradeFrequency;
            return this;
        }

        public Builder setSodaFrequency(int sodaFrequency) {
            this.sodaFrequency = sodaFrequency;
            return this;
        }

        public Builder setTaraFrequency(int taraFrequency) {
            this.taraFrequency = taraFrequency;
            return this;
        }

        public Builder setTemperatureFrequency(int temperatureFrequency) {
            this.temperatureFrequency = temperatureFrequency;
            return this;
        }

        public Builder setUnitFrequency(int unitFrequency) {
            this.unitFrequency = unitFrequency;
            return this;
        }

        public Builder setMetaWeight(int metaWeight) {
            this.metaWeight = metaWeight;
            return this;
        }

        public Builder setTailWeight(int tailWeight) {
            this.tailWeight = tailWeight;
            return this;
        }

        public NomenclatureFrequency Build() {
            return new NomenclatureFrequency(this);
        }

    }

    public NomenclatureFrequency(Builder builder) {

        this.forecastParseNom = builder.forecastParseNom;
        this.brandFrequency = builder.brandFrequency;
        this.catalogFrequency = builder.catalogFrequency;
        this.gradeFrequency = builder.gradeFrequency;
        this.sodaFrequency = builder.sodaFrequency;
        this.taraFrequency = builder.taraFrequency;
        this.temperatureFrequency = builder.temperatureFrequency;
        this.unitFrequency = builder.unitFrequency;
        this.metaWeight = builder.metaWeight;
        this.tailWeight = builder.tailWeight;

    }

}
