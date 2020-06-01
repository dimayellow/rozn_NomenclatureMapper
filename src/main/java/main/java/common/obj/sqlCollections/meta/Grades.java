package main.java.common.obj.sqlCollections.meta;

import main.java.common.obj.sqlCollections.SQLCollections;
import main.java.common.obj.sqlObjects.meta.Grade;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class Grades extends SQLCollections<Grade> {
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
