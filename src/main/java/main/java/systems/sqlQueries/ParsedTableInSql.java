package main.java.systems.sqlQueries;

import main.java.managers.NomenclatureStringParser;
import main.java.sqlObjects.SQLBaseObject;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.LinkedList;

public class ParsedTableInSql implements Runnable {

    private final LinkedList<NomenclatureStringParser> parserList;

    public ParsedTableInSql(LinkedList<NomenclatureStringParser> parserList) {
        this.parserList = new LinkedList<NomenclatureStringParser>();
        for (NomenclatureStringParser parser : parserList) {
            this.parserList.add(parser);
        }
    }

    @Override
    public void run() {
        startInsertProcessInSql();
    }

    private void startInsertProcessInSql() {

        String insertString = "INSERT INTO [dbo].[ForecastTovarParse]" +
                                                "([TovarId] " +
                                                ",[ForecastTovarTitle] " +
                                                ",[CountTitles] " +
                                                ",[BrandId] " +
                                                ",[CatalogId] " +
                                                ",[GradeId] " +
                                                ",[SodaId] " +
                                                ",[TaraId] " +
                                                ",[TemperatureId] " +
                                                ",[UnitId] " +
                                                ",[CountUnit] " +
                                                ",[Tails])\n";
        String valueString = "VALUES (%d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, %s, '%s')\n";

        System.out.println("Начало: " + Thread.currentThread().getId());

        String query = "";

        for (NomenclatureStringParser parser : parserList) {
            query += insertString;
            query += String.format(valueString, parser.getForecastTovarId(),
                                                parser.getStringForParse(),
                                                parser.getForecastTovarCount(),
                                                getId(parser.getBrand()),
                                                getId(parser.getCatalog()),
                                                getId(parser.getGrade()),
                                                getId(parser.getSoda()),
                                                getId(parser.getTara()),
                                                getId(parser.getTemperature()),
                                                getId(parser.getUnit()),
                                                parser.getCountUnitName(),
                                                parser.getTails()
                                                );
        }

        try {
            SQLBaseQuery.getInstance().InsertInSql(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Конец: " + Thread.currentThread().getId());
    }

    private int getId(SQLBaseObject baseObject) {
        int reply = 0;

        if (baseObject != null) reply = baseObject.getId();

        return reply;
    }

}
