package main.java.managers;

import main.java.other.enums.PartsOfString;
import main.java.common.obj.sqlCollections.*;
import main.java.common.obj.sqlCollections.meta.*;
import main.java.common.obj.sqlObjects.*;
import main.java.common.obj.sqlObjects.meta.*;
import main.java.other.MyProjectSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NomenclatureStringParser {

    private final String stringForParse;
    private final int forecastTovarId;
    private final int forecastTovarCount;
    private String stringRest = "";

    private HashMap<String, PartsOfString> partsNotFound = new HashMap<>();
    private HashMap<PartsOfString, String> partsOfNomenclatureString = new HashMap<>();

    private String tails = "";

    private Brand brand = null;
    private Unit unit = null;
    private Catalog catalog = null;
    private Soda soda = null;
    private Container tara = null;
    private Temperature temperature = null;
    private Grade grade = null;

    double countUnitName = 0.0;

    public NomenclatureStringParser(String stringForParse, int forecastTovarId, int forecastTovarCount) {
        this.stringForParse = stringForParse.trim();
        this.forecastTovarId = forecastTovarId;
        this.forecastTovarCount = forecastTovarCount;
        stringRest = stringForParse.toLowerCase();
    }

    public NomenclatureStringParser(String stringForParse) {
        this(stringForParse, -1, 0);
    }

    public String getStringForParse() {
        return stringForParse;
    }

    public void parseString() throws SQLException {

        findBrandInNomenclatureString();
        findCatalogInNomenclatureString();

        EnumSet.allOf(PartsOfString.class).forEach(this::findValueByRegEx);
        if (partsOfNomenclatureString.containsKey(PartsOfString.UNIT_WITH_COUNT)) {
            splitUpUnitAndCount();
        }
        fillObjectsByMap();
        parseTails();
    }

    public void printParseInformation() {
        System.out.println("Изначальная строка: " + stringForParse);
        System.out.println("Остаток строки: " + stringRest);
        System.out.println("Список найденных строк:");
        for (Map.Entry<PartsOfString, String> part : partsOfNomenclatureString.entrySet()) {
            System.out.printf("%30s : ", part.getKey().toString());
            System.out.printf("%50s |", part.getValue());
            System.out.println();
        }
        System.out.println("Найденный бренд: " + getObjectString(brand));
        System.out.println("Каталог: " + getObjectString(catalog));
        System.out.println("Найденная ед. изм.: " + getObjectString(unit));
        System.out.println("Газировка: " + getObjectString(soda));
        System.out.println("Тара: " + getObjectString(tara));
        System.out.println("Сорт: " + getObjectString(grade));
        System.out.println("Температура: " + getObjectString(temperature));
        System.out.println("Количество ед. :" + countUnitName);


    }

    public HashMap<String, PartsOfString> getPartsNotFound() {
        return partsNotFound;
    }

    public String getStringRest() {
        return stringRest;
    }

    public String getTails() {
        return tails;
    }

    public int getForecastTovarId() {
        return forecastTovarId;
    }

    public int getForecastTovarCount() {
        return forecastTovarCount;
    }

    public Brand getBrand() {
        return brand;
    }

    public Unit getUnit() {
        return unit;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public Soda getSoda() {
        return soda;
    }

    public Container getTara() {
        return tara;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Grade getGrade() {
        return grade;
    }

    public String getCountUnitName() {
        return Double.valueOf(countUnitName).toString().replace(",", ".");
    }

    public void parseTails() {
        Tails tailsList = Tails.getInstance();
        String[] subs = stringRest.split(" ");
        for (String sub : subs) {
            if (sub.equals("") || sub.equals(" ") || sub == null) continue;
            Integer reply = tailsList.getId(sub);
            if (reply != null) tails += reply.toString() + " ";
        }
        tails = tails.trim();
    }

    private void findSQLObjectInMap(SQLCollections sqlCollection, PartsOfString part, boolean regExFind) throws SQLException {

        if (!partsOfNomenclatureString.containsKey(part)) return;

        String partName = partsOfNomenclatureString.get(part).trim();

        sqlCollection.fillIn(true);

        boolean objectFind = false;
        for (SQLBaseObject sqlBaseObject : ((SQLCollections<SQLBaseObject>) sqlCollection).getList()) {
            for (String sboNameVariation : sqlBaseObject.getNames()) {
                objectFind = findPartNameInVariation(sboNameVariation, partName, regExFind);

                if (objectFind) {
                    variableByPartName(sqlBaseObject, part);
                    break;
                }
            }
            if (objectFind) break;
        }
        if (!objectFind) {
            partsNotFound.put(partName, part);
        }

    }

    private void findSQLObjectInMap(SQLCollections sqlCollection, PartsOfString part) throws SQLException {
        findSQLObjectInMap(sqlCollection, part, false);
    }

    private boolean findPartNameInVariation(String sboNameVariation , String partName, boolean regExFind) {
        boolean objectFind = false;
        if (regExFind) {
            Pattern pattern = Pattern.compile(sboNameVariation);
            Matcher matcher = pattern.matcher(partName);

            objectFind = matcher.find();

        } else {
            if (partName.equals(sboNameVariation)) {
                objectFind = true;
            }
        }


        return objectFind;
    }

    private void fillObjectsByMap() throws SQLException {
        findSQLObjectInMap(Units.getInstance(), PartsOfString.UNIT_NAME);
        findSQLObjectInMap(Sodas.getInstance(), PartsOfString.SODA);
        findSQLObjectInMap(Containers.getInstance(), PartsOfString.TARA);
        findSQLObjectInMap(Grades.getInstance(), PartsOfString.GRADE, true);
        findSQLObjectInMap(Temperatures.getInstance(), PartsOfString.TEMPERATURE_CONDITIONS, true);
        if (partsOfNomenclatureString.containsKey(PartsOfString.COUNT_UNIT_NAME)) {
            try {
                String cUnit = partsOfNomenclatureString.get(PartsOfString.COUNT_UNIT_NAME).trim();
                cUnit = cUnit.replaceAll("\\D", ".");
                countUnitName = Double.parseDouble(cUnit);
            } catch (NumberFormatException e) {
                int f = 1;
            }

        }
        stringRest = stringRest.replaceAll("\\p{Punct}", "");
    }

    /*    Функция заполнения реквизита из коллекции
    */
    private void variableByPartName(SQLBaseObject baseObject, PartsOfString part) {
        switch (part) {
            case GRADE:
                grade = (Grade) baseObject;
                break;
            case TARA:
                tara = (Container) baseObject;
                break;
            case SODA:
                soda = (Soda) baseObject;
                break;
            case BRAND:
                brand = (Brand) baseObject;
                break;
            case CATALOG:
                catalog = (Catalog) baseObject;
                break;
            case UNIT_NAME:
                unit = (Unit) baseObject;
                break;
            case TEMPERATURE_CONDITIONS:
                temperature = (Temperature) baseObject;
                break;
        }
    }

    /* Поиск бренда в названии номенклатуры.
   * поиск выполняется по полному вхождению названия бренда в строку
   */
    private void findBrandInNomenclatureString() throws SQLException {

        Brands brands = Brands.getInstance();
        brands.fillIn(true);

        boolean brandFound = false;
        for (Brand brand : brands.getList()) {
            for (String brandNameVariations : brand.getNames()) {
                if (stringRest.contains(brandNameVariations)) {
                    this.brand = brand;
                    brandFound = true;
                    fillInPartsOfNomenclatureString(PartsOfString.BRAND, brandNameVariations);
                    delValueFromStringRest(brandNameVariations);

                    break;
                }
            }
            if (brandFound) break;
        }
    }

    public ForecastParseNom createForecastParseNom() {
        ForecastParseNom.Builder builder =  new ForecastParseNom.Builder(stringForParse);

        if (brand != null) builder.setBrandId(brand.getId());
        if (unit != null) builder.setUnit(unit.getId());
        if (catalog != null) builder.setCatalog(catalog.getId());
        if (soda != null) builder.setSoda(soda.getId());
        if (tara != null) builder.setTara(tara.getId());
        if (temperature != null) builder.setTemperature(temperature.getId());
        if (grade != null) builder.setGrade(grade.getId());

        builder.setCountUnitName(getCountUnitName());
        builder.setTailList(tails);
        return builder.build();

    }

    private void findCatalogInNomenclatureString() throws SQLException {
        Catalogs catalogs = Catalogs.getInstance();
        catalogs.fillIn(true);

        boolean catalogFound = false;
        for (Catalog catalog : catalogs.getList()) {
            for (String catalogNameVariation : catalog.getNames()) {
                if (stringRest.contains(catalogNameVariation)) {
                    this.catalog = catalog;
                    catalogFound = true;
                    delValueFromStringRest(catalogNameVariation);

                    break;
                }
            }
            if (catalogFound) break;
        }
    }

    /* Поиск в строке значения по регулярному выражению.
    *  После этого найденное значение удаляется из строки.
     */
    private void findValueByRegEx(PartsOfString partOfString) {
        final String unitFindRegEx = partOfString.getRegEx();

        if (unitFindRegEx.equals("")) return;

        Pattern pattern = Pattern.compile(unitFindRegEx);
        Matcher matcher = pattern.matcher(stringRest);
        if (matcher.find()) {
            String findUnit = stringRest.substring(matcher.start(), matcher.end());
            this.fillInPartsOfNomenclatureString(partOfString, findUnit);
        }
        delValueFromStringRest(unitFindRegEx);
    }

    /* Разделяет уже найденное объединенное значение единицы и количества.
     */
    private void splitUpUnitAndCount() {

        String thisRegEx = "";
        String valueWithCount = partsOfNomenclatureString.get(PartsOfString.UNIT_WITH_COUNT);

        valueWithCount = valueWithCount.replace(",", ".");
        PartsOfString[] parts = {PartsOfString.UNIT_NAME, PartsOfString.COUNT_UNIT_NAME};

        for (PartsOfString part : parts) {

            thisRegEx = part == PartsOfString.UNIT_NAME ? part.getRegEx() : "\\d+(.|,)?\\d+";

            Pattern pattern = Pattern.compile(thisRegEx);
            Matcher matcher = pattern.matcher(valueWithCount);
            if (matcher.find()) {
                fillInPartsOfNomenclatureString(part,
                        valueWithCount.substring(matcher.start(), matcher.end())
                );
            }
        }
    }

    /* Отдельный метод, потому что логика может измениться.
     */
    private void fillInPartsOfNomenclatureString(PartsOfString thisPart, String value) {
        partsOfNomenclatureString.put(thisPart, value);
    }

    private void delValueFromStringRest(String value) {
        stringRest = stringRest.replace(value, "");
    }

    public String getObjectString(SQLBaseObject object) {
        if (object == null) return "Не найден";
        return object.toString();
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
        testStringParser.printParseInformation();
        System.out.println(testStringParser.createForecastParseNom().toString());
    }

}