package main.java.sqlObjects;

import java.util.Objects;

public class Grade extends SQLBaseObject {

    public Grade(String id) {
        this.id = Integer.parseInt(id);
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
