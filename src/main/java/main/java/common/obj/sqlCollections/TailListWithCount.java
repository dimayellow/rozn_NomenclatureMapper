package main.java.common.obj.sqlCollections;

import java.util.LinkedList;

public class TailListWithCount {
    private final LinkedList<Integer> tailForFrequencies;
    private final int count;
    private final int id;

    public TailListWithCount(int id, LinkedList<Integer> tailForFrequencies, int count) {
        this.tailForFrequencies = tailForFrequencies;
        this.count = count;
        this.id = id;
    }

    public LinkedList<Integer> getTailForFrequencies() {
        return tailForFrequencies;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }
}
