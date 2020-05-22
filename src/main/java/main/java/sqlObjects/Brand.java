package main.java.sqlObjects;

import java.util.Objects;

public class Brand extends SQLBaseObject{

    public Brand(String id) {
        this.id = Integer.parseInt(id);
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
