package main.java.sqlCollections;

import main.java.sqlObjects.SQLBaseObject;
import main.java.systems.SQLBaseQuery;
import main.java.systems.sqlQueries.SQLQueries;

import java.sql.SQLException;
import java.util.HashMap;

public class Tails {

    private static Tails instance;
    private HashMap<String, Integer> tails;

    private Tails() {
        try {
            tails = SQLBaseQuery.getInstance().getTailsFromSQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Tails getInstance() {
        if (instance == null) {
            instance = new Tails();
        }
        return instance;
    }

    public int getId(String word) {
        return tails.get(word);
    }

}
