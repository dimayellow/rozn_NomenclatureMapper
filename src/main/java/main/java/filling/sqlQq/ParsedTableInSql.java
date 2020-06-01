package main.java.filling.sqlQq;

import main.java.managers.NomenclatureStringParser;
import main.java.common.obj.sqlObjects.SQLBaseObject;
import main.java.systems.SQLBaseQuery;

import java.io.*;
import java.sql.SQLException;
import java.util.LinkedList;

public class ParsedTableInSql implements Runnable {

    private final LinkedList<NomenclatureStringParser> parserList;
    private static volatile int fileNum = 0;

    public ParsedTableInSql(LinkedList<NomenclatureStringParser> parserList) {
        this.parserList = new LinkedList<>();
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
                                                replaceStringForSql(parser.getStringForParse()),
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
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/dmitryk/errors/errorSql" + fileNum++ + ".sql"), "UTF-8"));) {
                out.write(query);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Файл не записан");
            } catch (FileNotFoundException e) {
                System.out.println("Файл не записан");
            } catch (IOException e) {
                System.out.println("Файл не записан");
            }
            throwables.printStackTrace();
        }
        System.out.println("Конец: " + Thread.currentThread().getId());
    }

    private int getId(SQLBaseObject baseObject) {
        int reply = 0;

        if (baseObject != null) reply = baseObject.getId();

        return reply;
    }

    private String replaceStringForSql(String stringForSQLQuery) {
        String reply = stringForSQLQuery.replaceAll("'", "''");
        return reply;
    }

}
