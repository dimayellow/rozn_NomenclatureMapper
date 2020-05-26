package main.java.systems.sqlQueries;

import main.java.sqlObjects.Brand;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContainerQuery implements SQLQueries<Brand> {

    @Override
    public String getQuery() {
        return "Select\n" +
                "Tara.TaraId as id,\n" +
                "isnull(Tara.Synonyms, '') as syns,\n" +
                "Tara.Title as title\n" +
                "from dbo.Tara as Tara";
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
