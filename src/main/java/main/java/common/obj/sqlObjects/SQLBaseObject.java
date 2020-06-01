package main.java.common.obj.sqlObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SQLBaseObject {
    protected int id;
    protected List<String> names = new ArrayList<>();
    protected boolean isNew;

    public List<String> getNames() {
        return names;
    }

    public int getId(){
        return id;
    }

    public void addName(String name) {
        if (!name.equals("")) this.names.add(name.toLowerCase().trim());
    }

    public void addNamesFromString(String brandVariations) {
        if (brandVariations.equals("")) return;
        String splitSymbol = " \\| ";
        for (String brand : brandVariations.split(splitSymbol)){
            if (!brand.equals(""))
                this.addName(brand);
        }
    }

    public void sort() {
        names.sort((s1, s2) -> Integer.compare(s2.length(),s1.length()));
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", names=" + names +
                '}';
    }

    public int hashCode() {
        return Objects.hash(id, names);
    }
}