package main.java.sqlCollections;

import main.java.sqlObjects.ForecastTovar;
import main.java.systems.SQLBaseQuery;

import java.sql.SQLException;

public class ForecastTovars extends SQLCollections<ForecastTovar> {

    private static ForecastTovars instance;

    private ForecastTovars() {
    }

    public static ForecastTovars getInstance() {
        if (instance == null) {
            instance = new ForecastTovars();
        }
        return instance;
    }

    public void fillIn() throws SQLException {
        super.fillIn();
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        sqlBaseManager.fillForecastTovarsFromSQL();
    }
}
