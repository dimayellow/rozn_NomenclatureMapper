package main.java.filling;

import main.java.common.obj.sqlCollections.Tails;
import main.java.filling.sqlQq.TailsInSql;
import main.java.managers.NomenclatureStringParser;
import main.java.common.obj.sqlCollections.ForecastTovars;
import main.java.common.obj.sqlObjects.ForecastTovar;
import main.java.filling.sqlQq.ParsedTableInSql;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class AggregateSQL {

    private final LinkedList<Thread> threads = new LinkedList<>();
    private final LinkedList<NomenclatureStringParser> parserTempList = new LinkedList<>();

    private final ForecastTovars forecastTovars = ForecastTovars.getInstance();

    public void fillSQL() {

        Set<String> tailSet = new HashSet<>();

        System.out.println("Начало загрузки ForecastTovars");
        fillInForecastTovars();

        LinkedList<NomenclatureStringParser> parserList = new LinkedList<>();

        // В первий цикл парсим номенклатуры, и получаем хвосты.
        System.out.println("Начало парсинга");
        for (ForecastTovar forecastTovar : forecastTovars.getList()) {

            NomenclatureStringParser parser = new NomenclatureStringParser(forecastTovar.getName(), forecastTovar.getId(), forecastTovar.getCount());

            if (parseNomenclatureDone(forecastTovar, parser)) continue;

            parserTempList.add(parser);
            fillInTails(tailSet, parser);
        }

        // Записываем хвосты
        System.out.println("Начало записи хвостов");
        LinkedList<String> partTailForSqlInsert = new LinkedList<>();
        for (String tail : tailSet) {
            checkPartyToSendTailAndStartThreadIfDone(tail, partTailForSqlInsert);
        }
        if (partTailForSqlInsert.size() > 0) {
            startThread(new TailsInSql(partTailForSqlInsert));
        }

        System.out.println("Потоки созданы, ждем");
        joinThreads();
        threads.clear();
        // обновляем список хвостов и по уже распарсенным номенклатурам перезаполняем хвосты и отправляем в базу
        System.out.println("Загрузка номенклатур");
        Tails.refillTails();
        System.out.println("Хвосты загружены");
        LinkedList<NomenclatureStringParser> partParseNomForSqlInsert = new LinkedList<>();
        for (NomenclatureStringParser parser : parserTempList) {
            checkPartyToSendForecastNomAndStartThreadIfDone(parser, partParseNomForSqlInsert);
        }
        if (partParseNomForSqlInsert.size()> 0) {
            startThread(new ParsedTableInSql(partParseNomForSqlInsert));
        }
        System.out.println("Потоки созданы, ждем");
        joinThreads();
        System.out.println("Конец");
    }

    private void checkPartyToSendTailAndStartThreadIfDone(String tail, LinkedList<String> partTailForSqlInsert) {
        partTailForSqlInsert.add(tail);
        if (partTailForSqlInsert.size() % 2000 == 0) {
            startThread(new TailsInSql(partTailForSqlInsert));
            partTailForSqlInsert.clear();
        }
    }

    private void checkPartyToSendForecastNomAndStartThreadIfDone(NomenclatureStringParser parser, LinkedList<NomenclatureStringParser> parserList) {
        parser.parseTails();
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
