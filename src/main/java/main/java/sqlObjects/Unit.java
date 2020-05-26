package main.java.sqlObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Unit extends SQLBaseObject{

    public Unit(String id) {
        this.id = Integer.parseInt(id);
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