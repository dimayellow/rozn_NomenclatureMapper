package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Soda extends SQLBaseObject {

    public Soda(int id) {
        this.id = id;
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
