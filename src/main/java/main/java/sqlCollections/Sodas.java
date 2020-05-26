package main.java.sqlCollections;

import main.java.sqlObjects.Soda;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Sodas extends SQLCollections<Soda>{
    private static Sodas instance;

    private Sodas() {
    }

    public static Sodas getInstance() {
        if (instance == null) {
            instance = new Sodas();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillSodaFromSQL();
    }
}
