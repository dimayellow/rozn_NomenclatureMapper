package main.java.managers;

import main.java.systems.MyProjectSettings;
import main.java.enums.PartsOfString;
import main.java.systems.SQLBaseQuery;
import main.java.sqlCollections.Brands;
import main.java.sqlObjects.Brand;

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

    public NomenclatureStringParser(String stringForParse, boolean testCall) {
        this.stringForParse = stringForParse;
        this.testCall = testCall;
        stringRest = stringForParse.toLowerCase();
    }

    public NomenclatureStringParser(String stringForParse) {
        this(stringForParse, false);
    }


    private void findBrandInNomenclatureString(Brands brands){
        boolean brandFound = false;
        int i = 0;
        for (Brand brand : brands.getList()) {
            for (String brandNameVariations : brand.getNames()) {
                i++;
                if (stringRest.contains(brandNameVariations)) {
                    this.brand = brand;
                    brandFound = true;
                    fillInPartsOfNomenclatureString(PartsOfString.BRAND, brandNameVariations);
                    stringRest = stringRest.replaceAll(brandNameVariations, "");

                    break;
                }
            }
            if (brandFound) break;
        }
        System.out.println(i);
    }

    private String regExSwitcher(PartsOfString partOfString) {
        String regExReply = "";
        switch (partOfString) {
            case UNIT_WITH_COUNT : {
                regExReply = "(\\d+(.|,))?\\d+\\s?+(г|гр|кг|мл|л|грамм)\\b";
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
        stringRest = stringRest.replaceAll(unitFindRegEx, "");
    }

    private void additionalProcessing(){
        if (partsOfNomenclatureString.containsKey(PartsOfString.UNIT_WITH_COUNT)) {
            splitUpUnitAndCount();
        }

    }

    private void splitUpUnitAndCount() {
        String regExUnit = "(г|гр|кг|мл|л|грамм)";
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

    //--------------------------------------

    public static void main(String[] args) {

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

        ArrayList<PartsOfString> testList = new ArrayList<>();
        testList.add(PartsOfString.BRAND);

        Date startBrand = new Date();

        SQLBaseQuery sqlBaseManager = SQLBaseQuery.getInstance();
        Brands brands = Brands.getInstance();
        try {
            sqlBaseManager.brandTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Date endBrand = new Date();

        System.out.println("Заполнение Brands : " + (endBrand.getTime() - startBrand.getTime())/(double)1000);
        NomenclatureStringParser finalTestStringParser = testStringParser;

        Date startBrandFind = new Date();
        testStringParser.findBrandInNomenclatureString(brands);
        Date endBrandFind = new Date();

        System.out.println("Поиск Brands : " + (endBrandFind.getTime() - startBrandFind.getTime())/(double)1000);

        EnumSet.allOf(PartsOfString.class).forEach(finalTestStringParser::findValueByRegEx);
        testStringParser.fillInPartsOfNomenclatureString(PartsOfString.STRING_REST, testStringParser.stringRest);
        testStringParser.additionalProcessing();

        System.out.println(testStringParser.stringForParse);

        for (Map.Entry<PartsOfString, List<String>> part : testStringParser.partsOfNomenclatureString.entrySet()) {
            System.out.printf("%30s : ", part.getKey().toString());
            part.getValue().forEach(x -> System.out.printf("%50s |", x));
            System.out.println("");
        }
    }

}
