package main.java.sqlCollections;

import main.java.sqlObjects.Container;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Containers extends SQLCollections<Container> {
    private static Containers instance;

    private Containers() {
    }

    public static Containers getInstance() {
        if (instance == null) {
            instance = new Containers();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillContainersFromSQL();
    }
}
