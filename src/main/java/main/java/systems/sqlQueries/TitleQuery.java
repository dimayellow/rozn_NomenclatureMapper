package main.java.systems.sqlQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TitleQuery implements SQLQueries<String>{

    @Override
    public String getQuery() { 
        return "select\n" +
                "ForecastTovars.Title\n" +
                "from dbo.ForecastTovars as ForecastTovars\n" +
                "where ForecastTovars.Title is not null";
    }

    @Override
    public String getElement(ResultSet executeQuery) throws SQLException {
        return executeQuery.getString("Title").toLowerCase();
    }
}
