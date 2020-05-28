package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Catalog extends SQLBaseObject {

    public Catalog(int id) {
        new Catalog(id, false);
    }

    public Catalog(int id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Catalog)) return false;
        Catalog unit = (Catalog) o;
        return id == unit.id &&
                Objects.equals(names, unit.names);
    }

    @Override
    public String toString() {
        return "Catalog" + super.toString();
    }
}
