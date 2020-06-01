package main.java.systems;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLQueries<T>{
    public String getQuery();
    public T getElement(ResultSet executeQuery) throws SQLException;
   // public void insertElement(T Element) throws SQLException;
}
