package main.java.filling;

import main.java.common.obj.sqlCollections.Tails;
import main.java.filling.sqlQq.TailsInSql;
import main.java.managers.NomenclatureStringParser;
import main.java.common.obj.sqlCollections.ForecastTovars;
import main.java.common.obj.sqlObjects.ForecastTovar;
import main.java.filling.sqlQq.ParsedTableInSql;
import main.java.systems.SQLBaseQuery;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class AggregateSQL {

    private final LinkedList<Thread> threads = new LinkedList<>();

    private final ForecastTovars forecastTovars = ForecastTovars.getInstance();

    public void fiilTails() {
        Set<String> tailSet = new HashSet<>();
        System.out.println("Начало загрузки ForecastTovars");
        fillInForecastTovars();

        for (ForecastTovar forecastTovar : forecastTovars.getList()) {

            NomenclatureStringParser parser = new NomenclatureStringParser(forecastTovar.getName(), forecastTovar.getId(), forecastTovar.getCount());

            if (parseNomenclatureDone(forecastTovar, parser)) continue;
            fillInTails(tailSet, parser);
        }
        System.out.println("Начало записи хвостов");
        SQLBaseQuery.getInstance().insertTailes(tailSet);


    }

    public void fillSQL() {

        System.out.println("Начало загрузки ForecastTovars");
        fillInForecastTovars();

        LinkedList<NomenclatureStringParser> parserList = new LinkedList<>();

        System.out.println("Начало парсинга");
        for (ForecastTovar forecastTovar : forecastTovars.getList()) {

            NomenclatureStringParser parser = new NomenclatureStringParser(forecastTovar.getName(), forecastTovar.getId(), forecastTovar.getCount());
            if (parseNomenclatureDone(forecastTovar, parser)) continue;;
            checkPartyToSendForecastNomAndStartThreadIfDone(parser, parserList);
        }
        if (parserList.size()> 0) {
            startThread(new ParsedTableInSql(parserList));
        }

        System.out.println("Потоки созданы, ждем");
        joinThreads();
        System.out.println("Конец");
    }


    private void checkPartyToSendForecastNomAndStartThreadIfDone(NomenclatureStringParser parser, LinkedList<NomenclatureStringParser> parserList) {
        parserList.add(parser);
        if (parserList.size() % 500 == 0) {
            startThread(new ParsedTableInSql(parserList));
            parserList.clear();
        }
    }

    private void joinThreads() {
        threads.forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void fillInTails(Set<String> tailSet, NomenclatureStringParser parser) {
        String [] subs = parser.getStringRest().split(" ");
        for (String sub : subs) {
            if (!sub.equals("")) tailSet.add(sub);
        }
    }

    private boolean parseNomenclatureDone(ForecastTovar forecastTovar, NomenclatureStringParser parser) {
        try {
            parser.parseString();
        } catch (SQLException throwables) {
            System.out.println("Номенклатура: " + forecastTovar + " ошибка");
            throwables.printStackTrace();
            return true;
        }
        return false;
    }

    private void fillInForecastTovars() {
        try {
            forecastTovars.fillIn(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void startThread(Runnable sendRequest) {
        Thread thread = new Thread(sendRequest);
        thread.start();
        threads.add(thread);
    }
    
    public static void main(String[] args) {
        AggregateSQL aggregateSQL = new AggregateSQL();

        aggregateSQL.fillSQL();

    }

}
