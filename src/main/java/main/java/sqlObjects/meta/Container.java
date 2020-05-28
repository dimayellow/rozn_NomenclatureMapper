package main.java.sqlObjects.meta;

import main.java.sqlObjects.SQLBaseObject;

public class Container extends SQLBaseObject {

    public Container(int id) {
        new Container(id, false);
    }

    public Container(int id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }
    @Override
    public String toString() {
        return "Tara" +  super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
