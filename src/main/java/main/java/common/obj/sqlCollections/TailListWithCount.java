package main.java.common.obj.sqlCollections;

import java.util.LinkedList;

public class TailListWithCount {
    private final LinkedList<Integer> tailForFrequencies = new LinkedList<>();
    private int count = 0;
    private final int id;

    public TailListWithCount(int id, LinkedList<Integer> tailForFrequencies, int count) {
        this.tailForFrequencies.addAll(tailForFrequencies);
        this.count = count;
        this.id = id;
    }
    public TailListWithCount(int id) {
        this.id = id;
    }

    public void addTailInList(int tail) {
        tailForFrequencies.add(tail);
    }

    public void addCount() {
        this.count++;
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

    @Override
    public String toString() {
        return "TailListWithCount{" +
                "tailForFrequencies=" + tailForFrequencies +
                ", id=" + id +
                '}';
    }
}
