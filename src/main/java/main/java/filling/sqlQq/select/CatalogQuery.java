package main.java.filling.sqlQq.select;

import main.java.common.obj.sqlObjects.meta.Catalog;
import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogQuery implements SQLQueries<Catalog> {

    @Override
    public String getQuery() {
        return "select\n" +
                "cat.CatalogId as id,\n" +
                "cat.Title as title,\n" +
                "ISNULL(cat.Synonyms, '') as syns\n" +
                "from dbo.Catalogs as cat";
    }

    @Override
    public Catalog getElement(ResultSet executeQuery) throws SQLException {
        Catalog catalog = new Catalog(executeQuery.getInt("id"));
        catalog.addName(executeQuery.getString("title"));
        catalog.addNamesFromString(executeQuery.getString("syns"));
        catalog.sort();

        return catalog;
    }
}
