package main.java.find;

import main.java.common.obj.sqlObjects.ForecastParseNom;
import main.java.common.obj.sqlObjects.NomenclatureFrequency;
import main.java.common.sqlQQ.ForeCastParseNomQuery;
import main.java.managers.NomenclatureStringParser;
import main.java.other.MyProjectSettings;
import main.java.systems.SQLBaseQuery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class TovarIdFinder {

    private final ForecastParseNom numForFind;

    private final LinkedList<NomenclatureFrequency> nomenclatureFrequency = new LinkedList<>();


    public TovarIdFinder(ForecastParseNom numForFind) {
        this.numForFind = numForFind;
    }

    public TovarIdFinder(NomenclatureStringParser nomenclatureStringParser) {
        numForFind = nomenclatureStringParser.createForecastParseNom();
    }

    public String createSqlQuery() {
        return new ForeCastParseNomQuery().getQueryFromParseNom(numForFind);
    }

    public void findTovars() throws SQLException {
        SQLBaseQuery query = SQLBaseQuery.getInstance();
        ForeCastParseNomQuery foreCastParseNomQuery = new ForeCastParseNomQuery();

        ResultSet executeQuery = query.createResultSet(foreCastParseNomQuery.getQueryFromParseNom(numForFind));
        while (executeQuery.next()) {
            nomenclatureFrequency.add(foreCastParseNomQuery.getElement(executeQuery));
        }
    }

    public static void main(String[] args) throws SQLException {

        MyProjectSettings settings = MyProjectSettings.getInstance();
        String testFilePath = settings.getProjectPath() + "/SettingsDir/testFilePath";
        NomenclatureStringParser testStringParser = null;

        try(BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            String stringForParse = reader.readLine();
            testStringParser = new NomenclatureStringParser(stringForParse);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        if (testStringParser == null) return;

        testStringParser.parseString();

        TovarIdFinder tovarIdFinder = new TovarIdFinder(testStringParser);
        System.out.println(tovarIdFinder.createSqlQuery());
        System.out.println(testStringParser.getTails());

    }

}
