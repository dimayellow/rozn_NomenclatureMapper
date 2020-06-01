package main.java.learning;

import java.util.HashMap;

public class TailInserter implements Runnable {

    private final HashMap<Integer, Integer> map;
    private final int id;

    public TailInserter(HashMap<Integer, Integer> map, int id) {
        this.map = map;
        this.id = id;
    }

    @Override
    public void run() {

    }
}
