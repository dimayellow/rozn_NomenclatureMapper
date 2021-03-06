package main.java.filling.sqlQq.select;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TailQuery {

    public String getQuery() {
        return "SELECT [TailId] as Id\n" +
                "      ,[Name] as Title\n" +
                "  FROM [dbo].[tails]";
    }

    public void getElement(HashMap<String, Integer> map, ResultSet executeQuery) throws SQLException {

        int id = executeQuery.getInt("Id");
        String name = executeQuery.getString("Title");

        map.put(name, id);
    }
}
