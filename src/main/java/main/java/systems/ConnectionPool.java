package main.java.systems;

import main.java.other.MyProjectSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;

public class ConnectionPool {

    private String url;
    private Vector<Connection> availableConns = new Vector<>();
    private Vector<Connection> usedConns = new Vector<>();


    {
        MyProjectSettings settings = MyProjectSettings.getInstance();

        String instanceName = settings.getSqlIp();
        String databaseName = settings.getSqlBase();
        String userName     = settings.getSqlLogin();
        String pass         = settings.getSqlPas();

        String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
        this.url = String.format(connectionUrl, instanceName, databaseName, userName, pass);
    }

    public ConnectionPool(int initConnCnt) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < initConnCnt; i++) {
            availableConns.addElement(getConnection());
        }
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public synchronized Connection retrieve() {
        Connection newConn = null;
        if (availableConns.size() == 0) {
            newConn = getConnection();
        } else {
            newConn = availableConns.lastElement();
            availableConns.removeElement(newConn);
        }
        usedConns.addElement(newConn);
        return newConn;
    }

}