package main.java.managers;

import main.java.enums.PartsOfString;
import main.java.sqlCollections.ForecastTovars;
import main.java.sqlCollections.meta.*;
import main.java.sqlObjects.ForecastTovar;
import main.java.systems.MyProjectSettings;
import main.java.systems.SQLBaseQuery;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class AggregateSQL {

    private LinkedList<String> errorList = new LinkedList<>();

    private ForecastTovars forecastTovars = ForecastTovars.getInstance();

    HashMap<String, PartsOfString> partsNotFound = new HashMap<>();

    private Brands brands = Brands.getInstance();
    private Catalogs catalogs = Catalogs.getInstance();
    private Containers containers = Containers.getInstance();
    private Grades grades = Grades.getInstance();
    private Sodas sodas = Sodas.getInstance();
    private Units units = Units.getInstance();
    private Temperatures temperatures = Temperatures.getInstance();


    public void fillSQL() throws FileNotFoundException {

        //      String path = MyProjectSettings.getInstance().getProjectPath();

        //        PrintStream out = new PrintStream(new FileOutputStream(path + "/output.txt"));
        //        System.setOut(out);

        Date fDate = new Date();
        System.out.println("Загрузка forecastTovars");
        Set<String> tailSet = new HashSet<>();
        try {
            forecastTovars.fillIn(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Date sDate = new Date();
        System.out.println("Загрузка forecastTovars закончилась " + ((double) sDate.getTime() - fDate.getTime())/1000);

        boolean firstStep = true;
        int index = 0;
        int countFtovars = forecastTovars.getList().size();
        for (ForecastTovar forecastTovar : forecastTovars.getList()) {

            NomenclatureStringParser parser = new NomenclatureStringParser(forecastTovar.getName());
            if (firstStep) {
                fDate = new Date();
                System.out.println("Парсинг первой номенклатуры закончен " + ((double) fDate.getTime() - sDate.getTime())/1000);
                firstStep = false;
            }
            index++;
            try {
                parser.parseString();
            } catch (SQLException throwables) {
                System.out.println("Номенклатура: " + forecastTovar + " ошибка");
                throwables.printStackTrace();
                continue;
            }
            if ((index % 10000) == 0) {
                System.out.println("Выполнено: " + index + "/" + countFtovars);
            }
            String [] subs = parser.getStringRest().split(" ");
            for (String sub : subs) {
                if (!sub.equals("")) tailSet.add(sub);
            }
        }

        sDate = new Date();

        System.out.println("Начинаем загрузку хвостов " + ((double) sDate.getTime() - fDate.getTime())/1000);
        SQLBaseQuery.getInstance().insertTailes(tailSet);

        fDate = new Date();
        System.out.println("Готово " + ((double) fDate.getTime() - sDate.getTime())/1000);

    }
    
    public static void main(String[] args) {
        AggregateSQL aggregateSQL = new AggregateSQL();

        try {
            aggregateSQL.fillSQL();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
