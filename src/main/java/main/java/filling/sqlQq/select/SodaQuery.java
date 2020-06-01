package main.java.filling.sqlQq.select;

import main.java.common.obj.sqlObjects.meta.Soda;
import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SodaQuery implements SQLQueries<Soda> {
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
        Soda soda = new Soda(executeQuery.getInt("id"));
        soda.addName(executeQuery.getString("title"));
        soda.addNamesFromString(executeQuery.getString("syns"));
        soda.sort();

        return soda;
    }
}
