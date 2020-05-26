package main.java.systems.sqlQueries;

import main.java.sqlObjects.Soda;
import main.java.sqlObjects.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SodaQuery implements SQLQueries<Soda>{
    @Override
    public String getQuery() {
        return "Select\n" +
                "Soda.SodaId as id,\n" +
                "ISNULL(Soda.Synonyms, '') as syns,\n" +
                "Soda.Title as title\n" +
                "from dbo.Soda as Soda";
    }

    @Override
    public Soda getElement(ResultSet executeQuery) throws SQLException {
        Soda soda = new Soda(executeQuery.getString("id"));
        soda.addName(executeQuery.getString("title"));
        soda.addNamesFromString(executeQuery.getString("syns"));
        soda.sort();

        return soda;
    }
}
