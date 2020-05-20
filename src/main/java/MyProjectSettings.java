package main.java;

import java.io.File;

public class MyProjectSettings {
    private static MyProjectSettings instance;
    private final String projectPath = new File(".").getAbsolutePath();

    private MyProjectSettings() {
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
