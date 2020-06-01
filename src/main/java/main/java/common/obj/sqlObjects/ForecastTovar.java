package main.java.common.obj.sqlObjects;

public class ForecastTovar {
    private int Id;
    private String name;
    private int count;

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public ForecastTovar(int id, String name, int count) {
        Id = id;
        this.name = name;
        this.count = count;
    }
}
