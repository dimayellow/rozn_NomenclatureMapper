package main.java.sqlCollections.meta;

import main.java.sqlCollections.SQLCollections;
import main.java.sqlObjects.meta.Catalog;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Catalogs extends SQLCollections<Catalog> {
    private static Catalogs instance;

    private Catalogs() {
    }

    public static Catalogs getInstance() {
        if (instance == null) {
            instance = new Catalogs();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillCatalogsFromSQL();
    }


}
