package main.java.common.obj.sqlObjects.meta;

import main.java.common.obj.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Brand extends SQLBaseObject {

    public Brand(int id) {
        this.id = id;;
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
    public String toString() {
        return "Brand" + super.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, names);
    }
}
