package main.java;

import java.sql.*;

public class SQLBase {

    //static final String JDBC_DRIVER = null;

    public static void main(String[] args) throws ClassNotFoundException {
        String instanceName = "192.168.0.235";
        String databaseName = "rozn";
        String userName = "DitryK";
        String pass = "1qaz@WSX";
        String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
        String connectionString = String.format(connectionUrl, instanceName, databaseName, userName, pass);
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try {
            // Подключение к базе данных
            Connection con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            Statement stmt = con.createStatement();
            ResultSet executeQuery = stmt.executeQuery("select top 10 \n" +
                    "Brand.title\n" +
                    "from dbo.Brands as Brand\n" +
                    "Where Brand.Stop = 0 and Brand.Title is not null");

            // Обход результатов выборки
            while (executeQuery.next()) {
                System.out.println(executeQuery.getString("Title"));
            }
            // Закрываем соединение
            executeQuery.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            // Обработка исключений
            ex.printStackTrace();
        }
    }

    
}
