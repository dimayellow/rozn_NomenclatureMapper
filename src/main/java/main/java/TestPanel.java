package main.java;

import main.java.managers.UnknownBrandsFinder;
import main.java.systems.MyProjectSettings;
import main.java.systems.SQLBaseQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestPanel {
    private TestPanel instance;
    private MyProjectSettings settings = MyProjectSettings.getInstance();
    private SQLBaseQuery sqlBaseQuery = SQLBaseQuery.getInstance();
    private BufferedReader reader;

    {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private TestPanel() {

    }

    public TestPanel getInstance() {
        if (instance == null) {
            instance = new TestPanel();
        }
        return instance;
    }

    private void printCommands(String commandEnum) {
        for (String s : commandEnum.split(";")) {
            System.out.println(s);
        }
    }



    //----------------------ОБЩИЕ------------------------

    private void mainPrintCommands() {
        printCommands("1 - список команд;2 - список уникальных брендов;0 - Выход");
    }

    //------------------ПОИСК БРЕНДОВ--------------------
    private void unknownBrandsFinder() throws IOException {
        UnknownBrandsFinder brandsFinder = UnknownBrandsFinder.getInstance();
        String command = "";
        boolean exit = false;

        while (exit) {

            switch (command = reader.readLine()) {
                case ("0") :
                    exit = true;
                    break;
                case ("1") :
                    unknownBrandsFinderPrintCommands();
                    break;
                case ("2") :
                    //testPanel.unknownBrandsFinder();
                    break;
                default:
                    System.out.println("Неизвестная команда");
            }
        }

    }



    private void unknownBrandsFinderPrintCommands() {
        printCommands("1 - список команд;2:[число] - Показать первые [число] товаров;3:Сохранить список в settings dir;0 - Назад");
    }


    //---------------------------------------------------

    public static void main(String[] args) throws IOException {

        TestPanel testPanel = new TestPanel();
        String command = "";
        boolean exit = false;

        testPanel.mainPrintCommands();
        while (exit) {

            switch (command = testPanel.reader.readLine()) {
                case ("0") :
                    exit = true;
                    break;
                case ("1") :
                    testPanel.mainPrintCommands();
                    break;
                case ("2") :
                    testPanel.unknownBrandsFinder();
                    break;
                default:
                    System.out.println("Неизвестная команда");
            }
        }
        testPanel.reader.close();
    }

}
