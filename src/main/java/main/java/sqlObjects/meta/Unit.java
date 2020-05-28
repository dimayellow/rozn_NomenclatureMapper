package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Unit extends SQLBaseObject {

    public Unit(int id) {
        new Unit(id, false);
    }

    public Unit(int id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return id == unit.id &&
                Objects.equals(names, unit.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, names);
    }

    @Override
    public String toString() {
        return "Unit" + super.toString();
    }

}