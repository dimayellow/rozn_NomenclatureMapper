package main.java.common.obj.sqlObjects.meta;

import main.java.common.obj.sqlObjects.SQLBaseObject;

public class Container extends SQLBaseObject {

    public Container(int id) {
        this.id = id;
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
