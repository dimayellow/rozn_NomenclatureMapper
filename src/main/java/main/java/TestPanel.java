package main.java;

import java.util.HashMap;

public class TestPanel {

    static void addStr(HashMap<String, Integer> map, int i, String s) {
        map.put(s, i);
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        addStr(map, 1, "odin");
        addStr(map, 2, "dva");
        Integer f = map.get("tri");
    }

}
