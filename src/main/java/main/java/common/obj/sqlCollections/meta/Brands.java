package main.java.common.obj.sqlCollections.meta;

import main.java.common.obj.sqlCollections.SQLCollections;
import main.java.common.obj.sqlObjects.meta.Brand;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Brands extends SQLCollections<Brand> {
    private static Brands instance;

    private Brands() {
    }

    public static Brands getInstance() {
        if (instance == null) {
            instance = new Brands();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillBrandsFromSQL();
    }

}
