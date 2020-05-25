package main.java.sqlCollections;

import main.java.sqlObjects.Unit;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Units {
    private static Units instance;
    private List<Unit> list = new LinkedList<>();

    public List<Unit> getList() {return list;}

    private Units() {
    }

    public static Units getInstance() {
        if (instance == null) {
            instance = new Units();
        }
        return instance;
    }

    public void add(Unit unit) { list.add(unit); }

    public Unit findByNumber(int id) {
        for (Unit unit : list) {
            if (unit.getId() == id) return unit;
        }
        return null;
    }

    public boolean isFilled() {
        return list.size() > 0;
    }

    public void fillIn() throws SQLException {
        list.clear();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.getUnitsTable();
    }

    public void fillIn(boolean needFillingCheck) throws SQLException {
        if (needFillingCheck & !isFilled()) {
            fillIn();
        }
    }

}
