package main.java.sqlCollections;

import main.java.sqlObjects.Catalog;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Catalogs {
    private static Catalogs instance;
    private List<Catalog> list = new LinkedList<>();

    public List<Catalog> getList() {return list;}

    private Catalogs() {
    }

    public static Catalogs getInstance() {
        if (instance == null) {
            instance = new Catalogs();
        }
        return instance;
    }

    public void add(Catalog catalog) { list.add(catalog); }

    public Catalog findByNumber(int id) {
        for (Catalog catalog : list) {
            if (catalog.getId() == id) return catalog;
        }
        return null;
    }

    public boolean isFilled() {
        return list.size() > 0;
    }

    public void fillIn() throws SQLException {
        list.clear();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.getCatalogsTable();
    }

    public void fillIn(boolean needFillingCheck) throws SQLException {
        if (needFillingCheck & !isFilled()) {
            fillIn();
        }
    }

}
