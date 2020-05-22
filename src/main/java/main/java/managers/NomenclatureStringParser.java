package main.java.managers;

import main.java.sqlCollections.Units;
import main.java.sqlObjects.Catalog;
import main.java.sqlObjects.SQLBaseObject;
import main.java.sqlObjects.Unit;
import main.java.systems.MyProjectSettings;
import main.java.enums.PartsOfString;
import main.java.systems.SQLBaseQuery;
import main.java.sqlCollections.Brands;
import main.java.sqlObjects.Brand;

import java.awt.image.ImageProducer;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NomenclatureStringParser {

    private String stringForParse;
    private String stringRest;
    private Map<PartsOfString, List<String>> partsOfNomenclatureString = new HashMap();
    boolean testCall;
    private Brand brand = null;
    private Unit unit = null;
    private Catalog catalog = null;

    public NomenclatureStringParser(String stringForParse, boolean testCall) {
        this.stringForParse = stringForParse;
        this.testCall = testCall;
        stringRest = stringForParse.toLowerCase();
    }

    public void parseString() throws SQLException {
        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();

        findBrandInNomenclatureString();
        // тут будет извлечение каталогов
        EnumSet.allOf(PartsOfString.class).forEach(this::findValueByRegEx);
        if (partsOfNomenclatureString.containsKey(PartsOfString.UNIT_WITH_COUNT)) {
            splitUpUnitAndCount();
        }
        findUnitInUnitString();


    }

    public void printParseInformation() {
        System.out.println("Изначальная строка: " + stringForParse);
        System.out.println("Остаток строки: " + stringRest);
        System.out.println("Список найденных строк:");
        for (Map.Entry<PartsOfString, List<String>> part : partsOfNomenclatureString.entrySet()) {
            System.out.printf("%30s : ", part.getKey().toString());
            part.getValue().forEach(x -> System.out.printf("%50s |", x));
            System.out.println("");
        }
        System.out.println("Найденный бренд: " + getObjectString(brand));
        System.out.println("Каталог: " + getObjectString(catalog));
        System.out.println("Найденная ед. изм.: " + getObjectString(unit));
    }

    public NomenclatureStringParser(String stringForParse) {
        this(stringForParse, false);
    }

    //-----------------PARSING------------------------------------

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

    private void findUnitInUnitString() throws SQLException {
        if (!partsOfNomenclatureString.containsKey(PartsOfString.UNIT_NAME)) return;
        String unitName = partsOfNomenclatureString.get(PartsOfString.UNIT_NAME).get(0);
        Units units = Units.getInstance();
        units.fillIn(true);

        boolean unitFound = false;
        for (Unit unit : units.getList()) {
            for (String unitNameVariations : unit.getNames()) {
                if (unitName.equals(unitNameVariations)) {
                    this.unit = unit;
                    unitFound = true;
                    break;
                }
            }
            if (unitFound) break;
        }
    }


    private String getUnitsEnum() { return "(г|гр|кг|мл|л|грамм)";}

    private String regExSwitcher(PartsOfString partOfString) {
        String regExReply = "";
        switch (partOfString) {
            case UNIT_WITH_COUNT : {
                regExReply = "(\\d+(.|,))?\\d+\\s?" + getUnitsEnum() + "\\b";
                break;
            }
            case PACKING: {
                regExReply = "(x|х)+\\d{1,3}"; //"(x|х)+\\s?+\\d{1,3}";
                break;
            }
            case SODA: {
                regExReply = "негаз| б/г|б/газ | газированная | газ |сил/газ";
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
                regExReply = "(ж/б)|с/б|c/б|(ст/б)|(стекло)|бутылка|стакан|м/у|в вак|пэт|д/пак|в/у|тетра";
                break;
            }
            case SORT: {
                regExReply = "((\\d-?(ый)?)|высш+.(ий)?)\\s?сорт|в/с";
                break;
            }
        }
        return regExReply;
    }

    private void findValueByRegEx(PartsOfString partOfString) {
        final String unitFindRegEx = regExSwitcher(partOfString);

        if (unitFindRegEx.equals("")) return;

        Pattern pattern = Pattern.compile(unitFindRegEx);
        Matcher matcher = pattern.matcher(stringRest);
        while (matcher.find()) {
            String findUnit = stringRest.substring(matcher.start(), matcher.end());
            this.fillInPartsOfNomenclatureString(partOfString, findUnit);
            if (testCall) System.out.println(findUnit);
        }
        delValueFromStringRest(unitFindRegEx);
    }

    private void splitUpUnitAndCount() {
        String regExUnit = getUnitsEnum();
        String regExCount = "\\d+(.|,)?\\d+";

        for (String valueWithCount : partsOfNomenclatureString.get(PartsOfString.UNIT_WITH_COUNT)) {
            Pattern pattern = Pattern.compile(regExUnit);
            Matcher matcher = pattern.matcher(valueWithCount);
            if (matcher.find()) fillInPartsOfNomenclatureString(PartsOfString.UNIT_NAME,
                    valueWithCount.substring(matcher.start(), matcher.end())
            );
            pattern = Pattern.compile(regExCount);
            matcher = pattern.matcher(valueWithCount);
            if (matcher.find()) fillInPartsOfNomenclatureString(PartsOfString.COUNT_UNIT_NAME,
                    valueWithCount.substring(matcher.start(), matcher.end())
            );
        }
    }

    private void fillInPartsOfNomenclatureString(PartsOfString thisPart, String value) {
        boolean mapHaveThisPart = partsOfNomenclatureString.containsKey(thisPart);
        List<String> valuesList = new ArrayList<>();
        if (!mapHaveThisPart) {
            valuesList.add(value);
        } else {
            valuesList = partsOfNomenclatureString.get(thisPart);
            valuesList.add(value);
        }
        partsOfNomenclatureString.put(thisPart, valuesList);
    }

    private void delValueFromStringRest(String value) {
        stringRest = stringRest.replaceAll(value, "");
    }

    //--------------------------------------

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
