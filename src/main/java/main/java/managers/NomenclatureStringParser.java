package main.java.managers;

import main.java.enums.PartsOfString;
import main.java.sqlCollections.*;
import main.java.sqlObjects.*;
import main.java.systems.MyProjectSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NomenclatureStringParser {

    private final String unitsRegExp = "(г|гр|кг|мл|л|грамм)";

    private final String stringForParse;
    private String stringRest;

    LinkedList<String> tails = new LinkedList<>();

    private HashMap<PartsOfString, String> partsOfNomenclatureString = new HashMap<>();

    private Brand brand = null;
    private Unit unit = null;
    private Catalog catalog = null;
    private Soda soda = null;

    private Container tara = null;
    private SQLBaseObject temperature = null;
    private SQLBaseObject Grade = null;

    private int countUnitName = 0;

    public NomenclatureStringParser(String stringForParse) {
        this.stringForParse = stringForParse;
        stringRest = stringForParse.toLowerCase();
    }

    public void parseString() throws SQLException {

        findBrandInNomenclatureString();
        findCatalogInNomenclatureString();

        EnumSet.allOf(PartsOfString.class).forEach(this::findValueByRegEx);
        if (partsOfNomenclatureString.containsKey(PartsOfString.UNIT_WITH_COUNT)) {
            splitUpUnitAndCount();
        }
        fillObjectsByMap();

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
    }


    private void findSQLObjectInMap(SQLCollections sqlCollection, PartsOfString part) throws SQLException {

        if (!partsOfNomenclatureString.containsKey(part)) return;

        String partName = partsOfNomenclatureString.get(part);

        sqlCollection.fillIn(true);

        boolean objectFind = false;
        for (SQLBaseObject sqlBaseObject : ((SQLCollections<SQLBaseObject>) sqlCollection).getList()) {
            for (String sboNameVariation : sqlBaseObject.getNames()) {
                if (partName.equals(sboNameVariation)) {
                    variableByPartName(sqlBaseObject, part);
                    objectFind = true;
                    break;
                }
            }
            if (objectFind) break;
        }
    }

    private void fillObjectsByMap() throws SQLException {
        findSQLObjectInMap(Units.getInstance(), PartsOfString.UNIT_NAME);
        findSQLObjectInMap(Sodas.getInstance(), PartsOfString.SODA);
        findSQLObjectInMap(Containers.getInstance(), PartsOfString.TARA);
        findSQLObjectInMap(Grades.getInstance(), PartsOfString.GRADE);
        findSQLObjectInMap(Temperatures.getInstance(), PartsOfString.TEMPERATURE_CONDITIONS);
        if (partsOfNomenclatureString.containsKey(PartsOfString.COUNT_UNIT_NAME)) {
            countUnitName = Integer.parseInt(partsOfNomenclatureString.get(PartsOfString.COUNT_UNIT_NAME));
        }
    }

    /*    Функция заполнения реквизита из коллекции
    */
    private void variableByPartName(SQLBaseObject baseObject, PartsOfString part) {
        switch (part) {
            case GRADE:
                Grade = baseObject;
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
            case UNIT_NAME:
                unit = (Unit) baseObject;
            case TEMPERATURE_CONDITIONS:
                temperature = baseObject;
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

    /* Поиск единицы измерения в распарсеной строке.
    * Ищется по полному соответствию среди уже найденной единицы измерения.
     */

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
        final String unitFindRegEx = regExSwitcher(partOfString);

        if (unitFindRegEx.equals("")) return;

        Pattern pattern = Pattern.compile(unitFindRegEx);
        Matcher matcher = pattern.matcher(stringRest);
        while (matcher.find()) {
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

        PartsOfString[] parts = {PartsOfString.UNIT_NAME, PartsOfString.UNIT_WITH_COUNT};

        for (PartsOfString part : parts) {

            thisRegEx = part == PartsOfString.UNIT_NAME ? unitsRegExp : "\\d+(.|,)?\\d+";

            Pattern pattern = Pattern.compile(thisRegEx);
            Matcher matcher = pattern.matcher(valueWithCount);
            if (matcher.find()) fillInPartsOfNomenclatureString(part,
                    valueWithCount.substring(matcher.start(), matcher.end())
            );
        }
    }

    /* Отдельный метод, потому что логика может измениться.
     */
    private void fillInPartsOfNomenclatureString(PartsOfString thisPart, String value) {
        partsOfNomenclatureString.put(thisPart, value);
    }

    private void delValueFromStringRest(String value) {
        stringRest = stringRest.replaceAll(value, "");
    }

    /* Список регулярных выражений, в зависимости от параметра.
     */
    private String regExSwitcher(PartsOfString partOfString) {
        String regExReply = "";
        switch (partOfString) {
            case UNIT_WITH_COUNT : {
                regExReply = "(\\d+(.|,))?\\d+\\s?" + unitsRegExp + "\\b";
                break;
            }
            case PACKING: {
                regExReply = "(x|х)+\\d{1,3}"; //"(x|х)+\\s?+\\d{1,3}";
                break;
            }
            case SODA: {
                regExReply = "негаз| б/г| б/газ | газированная | газ | сил/газ";
                break;
            }
            case TEMPERATURE_CONDITIONS: {
                regExReply = "охлажд[а-я]{1,10}|заморож[а-я]{1,10}";
                break;
            }
            case PERCENT: {
                regExReply = "((\\d+(.|,))?\\d%)";
                break;
            }
            case TARA: {
                regExReply = "(ж/б)|с/б|(ст/б)|(стекло)|бутылка|стакан|м/у|в вак|пэт|д/пак|в/у|тетра";
                break;
            }
            case GRADE: {
                regExReply = "((\\d-?(ый)?)|высш+.(ий)?)\\s?сорт|в/с";
                break;
            }
        }
        return regExReply;
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
    }

}