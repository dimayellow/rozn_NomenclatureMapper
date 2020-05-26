package main.java.systems.sqlQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLQueries<T>{
    public String getQuery();
    public T getElement(ResultSet executeQuery) throws SQLException;
}
