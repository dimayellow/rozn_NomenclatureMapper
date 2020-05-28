package main.java.sqlCollections.meta;

import main.java.sqlCollections.SQLCollections;
import main.java.sqlObjects.meta.Unit;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Units extends SQLCollections<Unit> {
    private static Units instance;

    private Units() {
    }

    public static Units getInstance() {
        if (instance == null) {
            instance = new Units();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillUnitsFromSQL();
    }

    public static void main(String[] args) throws SQLException {
        Units units = Units.getInstance();
        units.fillIn(true);
        int i = 1;
    }

}
