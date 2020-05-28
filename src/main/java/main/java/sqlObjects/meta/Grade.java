package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Grade extends SQLBaseObject {

    public Grade(int id) {
        new Grade(id, false);
    }

    public Grade(int id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade = (Grade) o;
        return id == grade.id &&
                Objects.equals(names, grade.names);
    }

    @Override
    public String toString() {
        return "Grade" + super.toString();
    }

}
