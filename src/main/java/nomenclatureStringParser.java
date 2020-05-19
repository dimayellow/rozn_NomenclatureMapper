package main.java;

import java.io.*;

public class nomenclatureStringParser {

    private String stringForParse;

    public nomenclatureStringParser(String stringForParse) {
        this.stringForParse = stringForParse;
    }

    public static void main(String[] args) throws FileNotFoundException {

        String testFilePath = "/home/dmitryk/IdeaProjects/rozn_NomenclatureMapper/src/testFilePath";
        nomenclatureStringParser testStringParser = null;

        try(BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            String stringForParse = reader.readLine();
            testStringParser = new nomenclatureStringParser(stringForParse);
            reader.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        if (testStringParser == null) return;



    }

    public String getUnit() {
        final String mainUnitRegExp = "";
        String reply = "";



        return reply;
    }

}
