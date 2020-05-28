package main.java.systems.sqlQueries;

import main.java.sqlObjects.meta.Temperature;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TemperatureQuery implements SQLQueries<Temperature> {

    @Override
    public String getQuery() {
        return "Select\n" +
                "Temperature.TemperatureId as id,\n" +
                "isnull(Temperature.Synonyms, '') as syns,\n" +
                "Temperature.Title as title\n" +
                "from dbo.Temperature as Temperature";
    }

    @Override
    public Temperature getElement(ResultSet executeQuery) throws SQLException {
        Temperature temperature = new Temperature(executeQuery.getInt("id"));
        temperature.addName(executeQuery.getString("title"));
        temperature.addNamesFromString(executeQuery.getString("syns"));
        temperature.sort();
        temperature.sort();

        return temperature;
    }
}
