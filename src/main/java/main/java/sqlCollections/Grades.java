package main.java.sqlCollections;

import main.java.sqlObjects.Grade;
import main.java.sqlObjects.Soda;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Grades extends SQLCollections<Grade>{
    private static Grades instance;

    private Grades() {
    }

    public static Grades getInstance() {
        if (instance == null) {
            instance = new Grades();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillGradesFromSQL();
    }
}
