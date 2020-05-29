package main.java.systems;

import main.java.sqlCollections.*;
import main.java.sqlCollections.meta.*;
import main.java.systems.sqlQueries.*;
import main.java.systems.sqlQueries.select.*;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class SQLBaseQuery {

    private static SQLBaseQuery instance;
    private final ConnectionPool pool;

    private HashMap<String, Integer> Tails;

    private SQLBaseQuery() {
        this.pool = new ConnectionPool(1);
    }

    public static SQLBaseQuery getInstance() {
        if (instance == null) {
            instance = new SQLBaseQuery();
        }
        return instance;
    }

    // Общие методы

    private ResultSet createResultSet(String request) throws SQLException {
        Connection connection = pool.retrieve();
        Statement statement = connection.createStatement();
        return statement.executeQuery(request);
    }

    public void InsertInSql(String request) throws SQLException {
        Connection connection = pool.retrieve();
        Statement statement = connection.createStatement();
        int count = statement.executeUpdate(request);
        System.out.println("Добавлено");
    }

    public LinkedList<String> getListFromSQL(SQLQueries sqlQueries) throws SQLException {
        LinkedList<String> titles = new LinkedList<>();
        ResultSet executeQuery = createResultSet(sqlQueries.getQuery());

        while (executeQuery.next()) {
            titles.add((String) sqlQueries.getElement(executeQuery));
        }

        return titles;
    }

    public void fillObjectsFromSQL(SQLCollections instance, SQLQueries sqlQueries) throws SQLException {
        ResultSet executeQuery = createResultSet(sqlQueries.getQuery());
        while (executeQuery.next()) {
            instance.add(sqlQueries.getElement(executeQuery));
        }
    }

    public HashMap<String, Integer> getTailsFromSQL() throws SQLException {
        TailQuery tailQuery = new TailQuery();
        HashMap<String, Integer> map = new HashMap<>();
        ResultSet executeQuery = createResultSet(tailQuery.getQuery());
        while (executeQuery.next()) {
            tailQuery.getElement(map, executeQuery);
        }
        return map;
    }

    // Методы по объектам

    public void fillBrandsFromSQL() throws SQLException {

        Brands brands = Brands.getInstance();
        fillObjectsFromSQL(Brands.getInstance(), new BrandQuery());
    }

    public void fillUnitsFromSQL() throws SQLException {
        fillObjectsFromSQL(Units.getInstance(), new UnitQuery());
    }

    public void fillCatalogsFromSQL() throws SQLException {
        fillObjectsFromSQL(Catalogs.getInstance(), new CatalogQuery());
    }

    public void fillSodaFromSQL() throws SQLException {

        fillObjectsFromSQL(Sodas.getInstance(), new SodaQuery());

    }

    public void fillContainersFromSQL() throws SQLException {

        fillObjectsFromSQL(Containers.getInstance(), new ContainerQuery());

    }

    public void fillGradesFromSQL() throws SQLException {

        fillObjectsFromSQL(Grades.getInstance(), new GradeQuery());

    }

    public void fillForecastTovarsFromSQL() throws SQLException {

        fillObjectsFromSQL(ForecastTovars.getInstance(), new ForecastTovarQuery());

    }

    public void fillTemperaturesFromSQL() throws SQLException {

        fillObjectsFromSQL(Temperatures.getInstance(), new TemperatureQuery());

    }

    public LinkedList<String> getTitleForecastTovars() throws SQLException {
        return getListFromSQL(new ForecastTovarsTitleQuery());
    }

    public void insertTailes(Set<String> tailSet){
        String insertQuery = "";

        int count = 0;
        for (String s : tailSet) {
            insertQuery+= "insert into dbo.tails (Name)\n";
            insertQuery+= "values ('" + s + "')\n";
            count++;
            if (count >= 1000) {
                try {
                    InsertInSql(insertQuery);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                insertQuery = "";
                count = 0;
            }
        }
        try {
            InsertInSql(insertQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    // Общие запросы

}