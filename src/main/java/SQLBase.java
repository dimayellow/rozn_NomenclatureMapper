package main.java;

import java.sql.*;

public class SQLBase {

    //static final String JDBC_DRIVER = null;

    public static void main(String[] args) {
        String instanceName = "192.168.0.235";
        String databaseName = "rozn";
        String userName = "DitryK";
        String pass = "1qaz@WSX";
        String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
        String connectionString = String.format(connectionUrl, instanceName, databaseName, userName, pass);
    }

    
}
