package main.java.systems.sqlQueries.select;

import main.java.sqlObjects.ForecastTovar;
import main.java.systems.sqlQueries.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForecastTovarQuery implements SQLQueries<ForecastTovar> {
    @Override
    public String getQuery() {
        return "select\n" +
                "tovars.TovarId as Id,\n" +
                "ftovars.Title as title,\n" +
                "count(ftovars.ForecastTovarId) as count\n" +
                "from dbo.Tovars as tovars\n" +
                "left join dbo.ForecastTovars as ftovars on tovars.TovarId = ftovars.TovarId\n" +
                "where ftovars.Title is not null\n" +
                "group by tovars.TovarId, ftovars.Title";
    }

    @Override
    public ForecastTovar getElement(ResultSet executeQuery) throws SQLException {
        int id = executeQuery.getInt("Id");
        int count = executeQuery.getInt("count");
        String title = executeQuery.getString("title");

        return new ForecastTovar(id, title, count);
    }
}
