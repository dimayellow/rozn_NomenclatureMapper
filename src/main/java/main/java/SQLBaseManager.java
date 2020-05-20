package main.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLBaseManager {

    private ConnectionPool pool;

    public SQLBaseManager() {
        this.pool = new ConnectionPool(1);
    }

    public List<String> brandTableQuery() throws SQLException {
        String originalRequest = "SELECT Brand.title FROM dbo.Brands as Brand WHERE Brand.Stop = 0 and Brand.Title is not null";
        List<String> reply = new ArrayList<>();

        Connection connection = pool.retrieve();
        Statement statement = connection.createStatement();
        ResultSet executeQuery = statement.executeQuery(originalRequest);

        while (executeQuery.next()) {
            reply.add(executeQuery.getString("Title"));
        }

        return reply;

    }

    public static void main(String[] args) {
    }

    
}
