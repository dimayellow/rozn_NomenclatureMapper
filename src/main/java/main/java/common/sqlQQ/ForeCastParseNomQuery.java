package main.java.common.sqlQQ;

import main.java.common.obj.sqlObjects.ForecastParseNom;
import main.java.common.obj.sqlObjects.NomenclatureFrequency;
import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ForeCastParseNomQuery implements SQLQueries<NomenclatureFrequency> {

    private ForecastParseNom forecastParseNom;
    private final StringBuilder queryBuilder = new StringBuilder();
    private final HashMap<String, String> conditionMap = new HashMap<>();

    private String countUnit = "0";
    private int unitId = 0;


    @Override
    public String getQuery() {
        return "SELECT [TovarId]\n" +
                "      ,[BrandFrequency]\n" +
                "      ,[CatalogFrequency]\n" +
                "      ,[GradeFrequency]\n" +
                "      ,[SodaFrequency]\n" +
                "      ,[TaraFrequency]\n" +
                "      ,[TemperatureFrequency]\n" +
                "      ,[UnitFrequency]\n" +
                "      ,[BrandId]\n" +
                "      ,[CatalogId]\n" +
                "      ,[GradeId]\n" +
                "      ,[SodaId]\n" +
                "      ,[TaraId]\n" +
                "      ,[TemperatureId]\n" +
                "      ,[UnitId]\n" +
                "      ,[CountUnit]\n" +
                "      ,[MetaWeight]\n" +
                "      ,[TailWeight]\n" +
                "      ,[Title]" +
                "  FROM [rozn].[dbo].[MetaFrequency]\n";
    }

    public String getQueryFromParseNom(ForecastParseNom nomenclature) {

        this.forecastParseNom = nomenclature;
        fiilInMapWithSelectedFilledDetails();
        countUnit = forecastParseNom.getCountUnitName();
        unitId = forecastParseNom.getUnit();

        queryBuilder.append(getQuery());
        AddQueryConditions();
        queryBuilder.append("))\n order by TovarId");
        return queryBuilder.toString();

    }

    private void AddQueryConditions() {

        if (conditionMap.size() != 0) {
            queryBuilder.append("WHERE ");
            if (unitId != 0) {
                addUnitCondition();
                queryBuilder.append(" and ");
            }
            queryBuilder.append("(( 1=1 ");
            addAllConditions();
            queryBuilder.append(")");
            if (conditionMap.size() > 1) {
                addAllOptionsForConditionsWithoutOneCondition();
            }
            queryBuilder.append(")");
        }
    }

    private void addAllConditions() {
        for (Map.Entry<String, String> condition: conditionMap.entrySet()) {
            chooseOptionFill(condition);
        }
    }

    private void addAllOptionsForConditionsWithoutOneCondition() {
        for (String excludedСondition : conditionMap.keySet()) {
            queryBuilder.append(" OR (( 1=1)");
            for (Map.Entry<String, String> condition: conditionMap.entrySet()) {
                if (!(excludedСondition.equals(condition.getKey()))) {
                    chooseOptionFill(condition);
                }
            }
        }
    }

    private void chooseOptionFill(Map.Entry<String, String> condition) {
        if (!condition.getKey().equals("[UnitId]")) {
            addTextQueryConditionByEntry(condition);
        }
    }

    private void addUnitCondition() {
      //  queryBuilder.append(" and ");
        queryBuilder.append(" [UnitId] = ");
        queryBuilder.append(unitId);
        queryBuilder.append(" and [CountUnit] = ");
        queryBuilder.append(countUnit);
    }

    private void addTextQueryConditionByEntry(Map.Entry<String, String> condition) {
        queryBuilder.append(" and ");
        queryBuilder.append(condition.getKey());
        queryBuilder.append(" = ");
        queryBuilder.append(condition.getValue());
    }

    private void fiilInMapWithSelectedFilledDetails() {
        for (Map.Entry<String, String> original: createMapOnForecastParseNomForQueryCreating().entrySet()) {
            if (!(original.getValue().equals("0"))) conditionMap.put(original.getKey(), original.getValue());
        }
    }

    private HashMap<String, String> createMapOnForecastParseNomForQueryCreating() {
        HashMap<String, String> reply = new HashMap<>();

        reply.put("[BrandId]", Integer.toString(forecastParseNom.getBrand()));
        reply.put("[CatalogId]", Integer.toString(forecastParseNom.getCatalog()));
        reply.put("[GradeId]", Integer.toString(forecastParseNom.getGrade()));
        reply.put("[SodaId]", Integer.toString(forecastParseNom.getSoda()));
        reply.put("[TaraId]", Integer.toString(forecastParseNom.getTara()));
        reply.put("[TemperatureId]", Integer.toString(forecastParseNom.getTemperature()));
       // reply.put("[UnitId]", Integer.toString(forecastParseNom.getUnit()));

        return reply;
    }

    @Override
    public NomenclatureFrequency getElement(ResultSet executeQuery) throws SQLException {

        ForecastParseNom nom = getForecastParseNomFromResultSet(executeQuery);

        int brandF = executeQuery.getInt("BrandFrequency");
        int catalogF = executeQuery.getInt("CatalogFrequency");
        int gradeF = executeQuery.getInt("GradeFrequency");
        int sodaF = executeQuery.getInt("sodaFrequency");
        int taraF = executeQuery.getInt("taraFrequency");
        int temperatureF = executeQuery.getInt("temperatureFrequency");
        int unitF = executeQuery.getInt("BrandFrequency");
        int metaWeight = executeQuery.getInt("MetaWeight");
        int tailWeight = executeQuery.getInt("TailWeight");

        NomenclatureFrequency.Builder builder = new NomenclatureFrequency.Builder(nom);
        return builder.setBrandFrequency(brandF).setCatalogFrequency(catalogF).setGradeFrequency(gradeF)
                .setSodaFrequency(sodaF).setTaraFrequency(taraF).setTemperatureFrequency(temperatureF).setUnitFrequency(unitF)
                .setMetaWeight(metaWeight).setTailWeight(tailWeight).Build();

    }

    private ForecastParseNom getForecastParseNomFromResultSet(ResultSet executeQuery) throws SQLException {
        ForecastParseNom.Builder forecastParseNomBuilder = new ForecastParseNom.Builder(executeQuery.getString("ForecastTovarTitle").toLowerCase());


        int brand = executeQuery.getInt("BrandId");
        int catalog = executeQuery.getInt("CatalogId");
        int grade = executeQuery.getInt("GradeId");
        int soda = executeQuery.getInt("SodaId");
        int tara = executeQuery.getInt("TaraId");
        int temperature = executeQuery.getInt("TemperatureId");
        int unit = executeQuery.getInt("UnitId");

        double d = executeQuery.getDouble("CountUnit");
        String count = Double.valueOf(d).toString().replace(",", ".");
        String tails = executeQuery.getString("Tails");
        int tovarId = executeQuery.getInt("TovarId");


        return forecastParseNomBuilder.setBrandId(brand).setCatalog(catalog).setGrade(grade).setSoda(soda)
                .setTara(tara).setTemperature(temperature).setUnit(unit).setCountUnitName(count)
                .setTailList(tails).setForecastTovarId(tovarId).build();
    }


}
