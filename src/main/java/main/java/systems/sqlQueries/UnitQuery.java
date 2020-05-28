package main.java.systems.sqlQueries;

import main.java.sqlObjects.meta.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitQuery implements SQLQueries<Unit> {

    @Override
    public String getQuery() {
        return "select \n" +
                "units.Synonyms as syns,\n" +
                "units.UnitId as id\n" +
                "from dbo.Units as units\n" +
                "where units.synonyms is not null";
    }

    @Override
    public Unit getElement(ResultSet executeQuery) throws SQLException {
        Unit unit = new Unit(executeQuery.getInt("id"));
        unit.addNamesFromString(executeQuery.getString("syns"));
        unit.sort();

        return unit;
    }
}
