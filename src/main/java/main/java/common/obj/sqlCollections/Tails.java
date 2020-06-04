package main.java.common.obj.sqlCollections;

import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.HashMap;

public class Tails {

    private static Tails instance;
    private HashMap<String, Integer> tails;

    private Tails() {
        refillInstance();
    }

    private void refillInstance() {
        try {
            tails = SQLBaseQuery.getInstance().getTailsFromSQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void refillTails() {
        Tails.getInstance().refillInstance();
    }

    public static Tails getInstance() {
        if (instance == null) {
            instance = new Tails();
        }
        return instance;
    }

    public int getId(String word) {
        int reply = 0;
        try {
            reply = tails.get(word);
        } catch (Exception ex) {
            reply = -1;
        }
        return reply;
    }

}
