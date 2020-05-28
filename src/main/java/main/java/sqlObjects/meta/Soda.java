package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Soda extends SQLBaseObject {

    public Soda(int id) {
        new Soda(id, false);
    }

    public Soda(int id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Soda)) return false;
        Soda soda = (Soda) o;
        return id == soda.id &&
                Objects.equals(names, soda.names);
    }

    @Override
    public String toString() {
        return "Soda" + super.toString();
    }

}
