package main.java.learning;

import main.java.common.obj.sqlCollections.TailListWithCount;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;


public class AggregateProbabilityTable {

    private final LinkedList<TailListWithCount> tailForFrequencyList;
    private LinkedList<Thread> threadList = new LinkedList<>();

    {
        LinkedList<TailListWithCount> tailForFrequencyListTemp;
        try {
            tailForFrequencyListTemp = SQLBaseQuery.getInstance().getTailList();
        } catch (SQLException ex) {
            tailForFrequencyListTemp = new LinkedList<>();
        }
        tailForFrequencyList = tailForFrequencyListTemp;
    }

    public void createMapAndStartThread() {

        for (TailListWithCount tailListWithCount : tailForFrequencyList) {
            handleTailListWithCountAndStartTread(tailListWithCount);
        }
        joinThreads();

    }

    private void handleTailListWithCountAndStartTread(TailListWithCount tailListWithCount) {
        HashMap<Integer, Integer> oneIdMap = new HashMap<>();
        for (Integer tailForFrequency : tailListWithCount.getTailForFrequencies()) {
            oneIdMap = addTailCountInMap(oneIdMap, tailForFrequency);
        }
        createNewThread(oneIdMap, tailListWithCount.getId(), tailListWithCount.getCount());
    }

    private void joinThreads() {
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private HashMap<Integer, Integer> addTailCountInMap(HashMap<Integer, Integer> oldMap, int tailForFrequency) {
        int count = (oldMap.containsKey(tailForFrequency)) ? oldMap.get(tailForFrequency) : 0;
        oldMap.put(tailForFrequency, ++count);
        return oldMap;
    }

    private void createNewThread(HashMap<Integer, Integer> mapForAnalyze, int currentId, int size) {
        TailInserter tailInserter = new TailInserter(mapForAnalyze, currentId, size);
       // tailInserter.run();
        Thread thread = new Thread(tailInserter);
        thread.start();
        threadList.add(thread);
    }


    public static void main(String[] args) {
        AggregateProbabilityTable aggregateProbabilityTable = new AggregateProbabilityTable();
        aggregateProbabilityTable.createMapAndStartThread();
    }
}
