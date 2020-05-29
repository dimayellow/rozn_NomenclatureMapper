package main.java.managers;

import main.java.sqlCollections.ForecastTovars;
import main.java.sqlObjects.ForecastTovar;
import main.java.systems.SQLBaseQuery;
import main.java.systems.sqlQueries.ParsedTableInSql;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class AggregateSQL {

    private LinkedList<String> errorList = new LinkedList<>();

    private ForecastTovars forecastTovars = ForecastTovars.getInstance();

    public void fillSQL() throws FileNotFoundException {

        Date fDate = new Date();
        System.out.println("Загрузка forecastTovars");
        Set<String> tailSet = new HashSet<>();
        try {
            forecastTovars.fillIn(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Date sDate = new Date();
        System.out.println("Загрузка forecastTovars закончилась " + ((double) sDate.getTime() - fDate.getTime())/1000);

        int index = 0;
        LinkedList<NomenclatureStringParser> parserList = new LinkedList<>();

        for (ForecastTovar forecastTovar : forecastTovars.getList()) {

            NomenclatureStringParser parser = new NomenclatureStringParser(forecastTovar.getName(), forecastTovar.getId(), forecastTovar.getCount());

            try {
                parser.parseString();
            } catch (SQLException throwables) {
                System.out.println("Номенклатура: " + forecastTovar + " ошибка");
                throwables.printStackTrace();
                continue;
            }
            index++;
            parserList.add(parser);
            if (index  >= 1000) {

                startThread(parserList);

                parserList.clear();
                index = 0;
            }

            String [] subs = parser.getStringRest().split(" ");
            for (String sub : subs) {
                if (!sub.equals("")) tailSet.add(sub);
            }
        }
        startThread(parserList);


    }

    private void startThread(LinkedList<NomenclatureStringParser> parserList) {
        ParsedTableInSql sendRequest = new ParsedTableInSql(parserList);
        Thread thread = new Thread(sendRequest);
        thread.start();
    }
    
    public static void main(String[] args) {
        AggregateSQL aggregateSQL = new AggregateSQL();

        try {
            aggregateSQL.fillSQL();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
