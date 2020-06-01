package main.java.common.obj.sqlObjects.meta;

import main.java.common.obj.sqlObjects.SQLBaseObject;

import java.util.Objects;

public class Grade extends SQLBaseObject {

    public Grade(int id) {
        this.id = id;
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
