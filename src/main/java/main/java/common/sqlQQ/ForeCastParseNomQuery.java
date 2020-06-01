package main.java.common.sqlQQ;

import main.java.common.obj.sqlObjects.ForecastParseNom;
import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForeCastParseNomQuery implements SQLQueries<ForecastParseNom> {


    @Override
    public String getQuery() {
        return "SELECT [TovarId]\n" +
                "      ,[ForecastTovarTitle]\n" +
                "      ,[CountTitles]\n" +
                "      ,[BrandId]\n" +
                "      ,[CatalogId]\n" +
                "      ,[GradeId]\n" +
                "      ,[SodaId]\n" +
                "      ,[TaraId]\n" +
                "      ,[TemperatureId]\n" +
                "      ,[UnitId]\n" +
                "      ,[CountUnit]\n" +
                "      ,[Tails]\n" +
                "      ,[ParseTovarId]\n" +
                "  FROM [rozn].[dbo].[ForecastTovarParse]";
    }

    public String getQueryFromParseNom(ForecastParseNom nomenclature) {
        String query = getQuery() + "\n";

        if (nomenclature.getBrand() != 0) {
            query += "where [BrandId]" + nomenclature.getBrand() + "\n";
        }
        if (nomenclature.getCatalog() != 0) {
            query += "where [CatalogId]" + nomenclature.getCatalog() + "\n";
        }
        if (nomenclature.getUnit() != 0) {
            query += "where [UnitId]" + nomenclature.getUnit() + "\n";
        }

        return query;

    }


    @Override
    public ForecastParseNom getElement(ResultSet executeQuery) throws SQLException {

        ForecastParseNom.Builder builder = new ForecastParseNom.Builder(executeQuery.getString("ForecastTovarTitle").toLowerCase());

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

        return builder.setBrandId(brand).setCatalog(catalog).setGrade(grade).setSoda(soda)
                .setTara(tara).setTemperature(temperature).setUnit(unit).setCountUnitName(count)
                .setTailList(tails).setForecastTovarId(tovarId).build();
    }
}
