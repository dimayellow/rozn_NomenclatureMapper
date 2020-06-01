package main.java.learning;

import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class AggregateProbabilityTable {

    private HashMap<Integer, Integer> tailMap;

    {
        try {
            tailMap = SQLBaseQuery.getInstance().getTailMap();
        } catch (SQLException ex) {
            tailMap = new HashMap<>();
        }
    }

    public void fiilInTailTable() {

        HashMap<Integer, Integer> oneIdMap = new HashMap<>();

        LinkedList<Thread> threadList= new LinkedList<>();

         //lastid = 0;

        for (Map.Entry<Integer,Integer> elem : tailMap.entrySet()) {

        }

    }

    private void createNewThread(HashMap<Integer, Integer> mapForAnalyze) {

    }

}
