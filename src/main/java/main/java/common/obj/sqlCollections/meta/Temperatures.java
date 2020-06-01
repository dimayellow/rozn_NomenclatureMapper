package main.java.common.obj.sqlCollections.meta;

import main.java.common.obj.sqlCollections.SQLCollections;
import main.java.common.obj.sqlObjects.meta.Temperature;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Temperatures extends SQLCollections<Temperature> {
    private static Temperatures instance;

    private Temperatures() {
    }

    public static Temperatures getInstance() {
        if (instance == null) {
            instance = new Temperatures();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillTemperaturesFromSQL();
    }
}
