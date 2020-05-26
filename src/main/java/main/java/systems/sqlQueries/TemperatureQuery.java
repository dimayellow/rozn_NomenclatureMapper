package main.java.systems.sqlQueries;

import main.java.sqlObjects.Brand;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TemperatureQuery implements SQLQueries<Brand> {

    @Override
    public String getQuery() {
        return "Select\n" +
                "Temperature.TemperatureId as id,\n" +
                "isnull(Temperature.Synonyms, '') as syns,\n" +
                "Temperature.Title as title\n" +
                "from dbo.Temperature as Temperature";
    }

    @Override
    public Brand getElement(ResultSet executeQuery) throws SQLException {
        Brand brand = new Brand(executeQuery.getString("id"));
        brand.addName(executeQuery.getString("rus"));
        brand.addName(executeQuery.getString("eng"));
        brand.addNamesFromString(executeQuery.getString("syns"));
        brand.sort();

        return brand;
    }
}
