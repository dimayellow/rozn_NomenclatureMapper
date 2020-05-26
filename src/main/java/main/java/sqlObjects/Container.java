package main.java.sqlObjects;

public class Container extends SQLBaseObject {

    public Container(String id) {
        this.id = Integer.parseInt(id);
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
