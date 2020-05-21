package main.java.systems;

import main.java.TestPanel;
import main.java.sqlCollections.Brands;
import main.java.sqlObjects.Brand;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLBaseQuery {

    private static SQLBaseQuery instance;

    private ConnectionPool pool;

    private SQLBaseQuery() {
        this.pool = new ConnectionPool(1);
    }

    public static SQLBaseQuery getInstance() {
        if (instance == null) {
            instance = new SQLBaseQuery();
        }
        return instance;
    }

    private ResultSet createResultSet(String request) throws SQLException {
        Connection connection = pool.retrieve();
        Statement statement = connection.createStatement();
        return statement.executeQuery(request);
    }

    //------------------Работа с таблицей брэндов--------------------------

    public void brandTable() throws SQLException {

        Brands brands = Brands.getInstance();

        ResultSet executeQuery = createResultSet(brandSqlQuery());

        while (executeQuery.next()) {
            brands.add(createBrandForQuerryString(executeQuery));
        }
    }

    private String brandSqlQuery() {
        return "Select\n" +
                "isnull(Brand.title, '') as rus,\n" +
                "isnull(Brand.Title_eng, '') as eng,\n" +
                "Brand.BrandId as id,\n" +
                "isnull(Brand.Synonyms, '') as syns\n" +
                "from dbo.Brands as Brand\n" +
                "Where Brand.Stop = 0\n" +
                "Order by Brand.[Go] desc";
    }

    private Brand createBrandForQuerryString(ResultSet executeQuery) throws SQLException {
        Brand brand = new Brand(executeQuery.getString("id"));
        brand.addName(executeQuery.getString("rus"));
        brand.addName(executeQuery.getString("eng"));
        brand.addNamesFromString(executeQuery.getString("syns"));
        brand.sort();

        return brand;
    }

    //---------------------------------------------------------------------
    //---------------------ВСЕ НАЗВАНИЯНОМЕНКЛАТУР-------------------------

    public List<String> getTitleForecastTovars() throws SQLException {
        List<String> titles = new ArrayList<>();
        ResultSet executeQuery = createResultSet(titleSqlQuery());

        while (executeQuery.next()) {
            titles.add(executeQuery.getString("Title").toLowerCase());
        }

        return titles;
    }


    private String titleSqlQuery() {
        return "select\n" +
                "ForecastTovars.Title\n" +
                "from dbo.ForecastTovars as ForecastTovars\n" +
                "where ForecastTovars.Title is not null";
    }

    //---------------------------------------------------------------------


    public static void main(String[] args) {
    }

    
}
