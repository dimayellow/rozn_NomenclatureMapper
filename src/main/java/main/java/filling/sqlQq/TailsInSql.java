package main.java.filling.sqlQq;

import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

public class TailsInSql implements Runnable{
    private final LinkedList<String> tailSet = new LinkedList<>();

    public TailsInSql(LinkedList<String> tailSet) {
        this.tailSet.addAll(tailSet);
    }

    @Override
    public void run() {
        StringBuilder insertQuery = new StringBuilder();
        for (String s : tailSet) {
            insertQuery.append("insert into dbo.tails (Name)\n");
            insertQuery.append("values ('" + s + "')\n");
            try {
                SQLBaseQuery.getInstance().InsertInSql(insertQuery.toString());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
