package main.java.sqlObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Brand {
    private int id;
    private List<String> names = new ArrayList<>();

    public List<String> getNames() {
        return names;
    }

    public int getId(){
        return id;
    }

    public Brand(String id) {
        this.id = Integer.parseInt(id);
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
        return "Brand{" +
                "id=" + id +
                ", names=" + names +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand)) return false;
        Brand brand = (Brand) o;
        return id == brand.id &&
                Objects.equals(names, brand.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, names);
    }
}
