package main.java.sqlCollections;

import main.java.sqlObjects.Brand;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.*;

public class Brands {
    private static Brands instance;
    private List<Brand> list = new ArrayList<>();

    public List<Brand> getList() {
        return list;
    }

    private Brands() {
    }

    public static Brands getInstance() {
        if (instance == null) {
            instance = new Brands();
        }
        return instance;
    }

    public void add(Brand brand) {
        list.add(brand);
    }

    public Brand findByNumber(int id) {
        for (Brand brand : list) {
            if (brand.getId() == id) return brand;
        }
        return null;
    }

    public boolean isFilled() {
        return list.size() > 0;
    }

    public void fillIn() throws SQLException {
        list.clear();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.brandTable();
    }


}
