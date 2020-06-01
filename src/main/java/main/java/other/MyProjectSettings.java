package main.java.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyProjectSettings {
    private static MyProjectSettings instance;
    private final String projectPath = new File(".").getAbsolutePath();
    private String sqlIp, sqlBase, sqlLogin, sqlPas;

    public String getSqlIp() {
        return sqlIp;
    }

    public String getSqlBase() {
        return sqlBase;
    }

    public String getSqlLogin() {
        return sqlLogin;
    }

    public String getSqlPas() {
        return sqlPas;
    }

    private MyProjectSettings() {
        String sqlSettingsPath = projectPath + "/SettingsDir/sqlSettings";
        String settingsString = "";
        try(BufferedReader reader = new BufferedReader(new FileReader(sqlSettingsPath))) {
            while ((settingsString = reader.readLine()) != null){
                String[] settingArray = settingsString.split("=");
                if (settingArray.length != 2) continue;
                fiilSqlSettings(settingArray);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void fiilSqlSettings(String[] settingArray) {

        switch (settingArray[0]) {
            case ("sqlIp"):
                sqlIp = settingArray[1];
                break;
            case ("sqlBase"):
                sqlBase = settingArray[1];
                break;
            case ("sqlLogin"):
                sqlLogin = settingArray[1];
                break;
            case ("sqlPas"):
                sqlPas = settingArray[1];
                break;
        }
    }

    public static MyProjectSettings getInstance() {
        if (instance == null) {
            instance = new MyProjectSettings();
        }
        return instance;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public static void main(String[] args) {

    }

}