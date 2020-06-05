package main.java.learning;

import main.java.systems.SQLBaseQuery;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TailInserter implements Runnable {

    private final HashMap<Integer, Integer> wordEntryStatistics = new HashMap<>();
    private final int id;
    private final int size;

    int fileNum = 0;
    private HashMap<Integer, Integer> percentMap = new HashMap<>();

    public TailInserter(HashMap<Integer, Integer> wordEntryStatistics, int id, int size) {
        this.wordEntryStatistics.putAll(wordEntryStatistics);
        this.id = id;
        this.size = size;
    }

    @Override
    public void run() {
        fillInPercentMap();
        insertSQL(createSQLQuery());
        System.out.println("Конец: " + Thread.currentThread().getId());
    }

    private void insertSQL(String queryText) {
        try {
            SQLBaseQuery.getInstance().InsertInSql(queryText);
        } catch (SQLException throwables) {
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/dmitryk/errors/errorSql_tail" + fileNum++ + ".sql"), "UTF-8"));) {
                out.write(queryText);
            } catch (Exception e) {
                System.out.println("Файл не записан");
            }
            throwables.printStackTrace();
        }
    }

    private void fillInPercentMap() {
        for (Map.Entry<Integer, Integer> tailEntry : wordEntryStatistics.entrySet()) {
            percentMap.put(tailEntry.getKey(), tailEntry.getValue() * 100 / size);
        }

    }

    private int sumSize() {
        AtomicInteger sum = new AtomicInteger();
        wordEntryStatistics.forEach((k, v) -> sum.addAndGet(v));
        return sum.get();
    }

    private String createSQLQuery() {
        StringBuilder query = new StringBuilder();
        String insertString = "INSERT INTO [dbo].[TailFrequency]\n" +
                                "           ([TovarId]\n" +
                                "           ,[TailId]\n" +
                                "           ,[Frequency])";
        String valueString = "VALUES (%d, %d, %d)\n";

        for (Map.Entry<Integer, Integer> tailEntry : percentMap.entrySet()) {
            query.append(insertString);
            query.append(String.format(valueString, id, tailEntry.getKey(), tailEntry.getValue()));
        }

        return query.toString();
    }


}
