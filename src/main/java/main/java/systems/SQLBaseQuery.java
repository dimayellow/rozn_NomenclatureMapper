package main.java.systems;

import main.java.sqlCollections.*;
import main.java.sqlObjects.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLBaseQuery {

    private static SQLBaseQuery instance;

    private final ConnectionPool pool;

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

    public void getBrandTable() throws SQLException {

        Brands brands = Brands.getInstance();
        ResultSet executeQuery = createResultSet(brandSqlQuery());
        while (executeQuery.next()) {
            brands.add(createBrandForQueryString(executeQuery));
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

    private Brand createBrandForQueryString(ResultSet executeQuery) throws SQLException {
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
    //---------------------------UNITS-------------------------------------

    public void getUnitsTable() throws SQLException {

        Units units = Units.getInstance();
        ResultSet executeQuery = createResultSet(unitsSqlQuery());
        while (executeQuery.next()) {
            units.add(createUnitForQuerryString(executeQuery));
        }
    }

    private Unit createUnitForQuerryString(ResultSet executeQuery) throws SQLException {
        Unit unit = new Unit(executeQuery.getString("id"));
        unit.addNamesFromString(executeQuery.getString("syns"));
        unit.sort();

        return unit;
    }

    private String unitsSqlQuery() {
        return "select \n" +
                "units.Synonyms as syns,\n" +
                "units.UnitId as id\n" +
                "from dbo.Units as units\n" +
                "where units.synonyms is not null";
    }

    //---------------------------------------------------------------------

    //--------------------------CATALOGS-----------------------------------

    public void getCatalogsTable() throws SQLException {
        Catalogs catalogs = Catalogs.getInstance();
        ResultSet executeQuery = createResultSet(catalogsSqlQuery());
        while (executeQuery.next()) {
            catalogs.add(createCatalogForQuerryString(executeQuery));
        }
    }
    
    private Catalog createCatalogForQuerryString(ResultSet executeQuery) throws SQLException {
        Catalog catalog = new Catalog(executeQuery.getString("id"));
        catalog.addName("title");
        catalog.addNamesFromString(executeQuery.getString("syns"));
        catalog.sort();

        return catalog;
    }
    
    private String catalogsSqlQuery() {
        return "select top 100\n" +
                "cat.CatalogId as id,\n" +
                "cat.Title as title,\n" +
                "ISNULL(cat.Synonyms, '') as syn\n" +
                "from dbo.Catalogs as cat";
    }

    //---------------------------------------------------------------------

}
