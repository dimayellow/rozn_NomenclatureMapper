package main.java;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NomenclatureStringParser {

    private String stringForParse;
    private Map<PartsOfString, List<String>> partsOfNomenclatureString = new HashMap();
    //private String mainUnit = "(г|гр|кг|мл|л|грамм)\\b";
    boolean testCall = false;

    public NomenclatureStringParser(String stringForParse, boolean testCall) {
        this.stringForParse = stringForParse;
        this.testCall = testCall;
    }

    public NomenclatureStringParser(String stringForParse) {
        this.stringForParse = stringForParse;
    }

    public static void main(String[] args) throws FileNotFoundException {

        String testFilePath = "/home/dmitryk/IdeaProjects/rozn_NomenclatureMapper/src/testFilePath";
        NomenclatureStringParser testStringParser = null;

        try(BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            String stringForParse = reader.readLine();
            testStringParser = new NomenclatureStringParser(stringForParse);
            reader.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        if (testStringParser == null) return;

        testStringParser.getUnit();



    }

    private void getUnit() {
        final String mainUnitRegExp = "";
        String reply = "(\\d+(.|,))?\\d+\\s?+(г|гр|кг|мл|л|грамм)\\b";

        Pattern pattern = Pattern.compile(reply);
        Matcher matcher = pattern.matcher(stringForParse);
        while (matcher.find()) {
            String findUnit = stringForParse.substring(matcher.start(), matcher.end());

            if (testCall) System.out.println(findUnit);
        }
        stringForParse = stringForParse.replaceAll(reply, "");
        System.out.println(stringForParse);
        //return reply;
    }

}
