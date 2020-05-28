package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Temperature extends SQLBaseObject {

    public Temperature(int id) {
        new Temperature(id, false);
    }

    public Temperature(int id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temperature)) return false;
        Temperature temperature = (Temperature) o;
        return id == temperature.id &&
                Objects.equals(names, temperature.names);
    }

    @Override
    public String toString() {
        return "Grade" + super.toString();
    }

}
