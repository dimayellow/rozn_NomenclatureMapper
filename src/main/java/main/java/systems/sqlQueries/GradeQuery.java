package main.java.systems.sqlQueries;

import main.java.sqlObjects.Soda;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeQuery implements SQLQueries<Soda>{
    @Override
    public String getQuery() {
        return "Select\n" +
                "Grade.GradeId as id,\n" +
                "ISNULL(Grade.Synonyms, '') as syns,\n" +
                "Grade.Title as title\n" +
                "from dbo.Grade as Grade";
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
