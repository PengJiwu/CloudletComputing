package config;

import java.io.*;
import java.util.Properties;


public class AppConfiguration {

    public static final String FILENAME = "";



    public static void readConfiguration() {
        try {

            Properties prop = new Properties();
            FileInputStream inputStream = new FileInputStream(FILENAME);
            prop.load(inputStream);
            // prop.getProperty("PropertyName"));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
