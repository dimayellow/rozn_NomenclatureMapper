package main.java.systems.sqlQueries;

import main.java.sqlObjects.meta.Brand;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BrandQuery implements SQLQueries<Brand> {

    @Override
    public String getQuery() {
        return "Select\n" +
                "isnull(Brand.title, '') as rus,\n" +
                "isnull(Brand.Title_eng, '') as eng,\n" +
                "Brand.BrandId as id,\n" +
                "isnull(Brand.Synonyms, '') as syns\n" +
                "from dbo.Brands as Brand\n" +
                "Where Brand.Stop = 0\n" +
                "Order by Brand.[Go] desc";
    }

    @Override
    public Brand getElement(ResultSet executeQuery) throws SQLException {
        Brand brand = new Brand(executeQuery.getInt("id"));
        brand.addName(executeQuery.getString("rus"));
        brand.addName(executeQuery.getString("eng"));
        brand.addNamesFromString(executeQuery.getString("syns"));
        brand.sort();

        return brand;
    }
}
