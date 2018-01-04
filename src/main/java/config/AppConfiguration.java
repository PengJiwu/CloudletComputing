package config;

import java.io.*;
import java.util.Properties;


public class AppConfiguration {

    public static final String FILENAME = "src/main/resources/application.properties";
    public static double SETUP_TIME = 0.0;
    public static int N = 0;
    public static int S = 0;
    public static double L1 = 0.0;
    public static double L2 = 0.0;
    public static double CLOUDLET_M1 = 0.0;
    public static double CLOUDLET_M2 = 0.0;
    public static double CLOUD_M1 = 0.0;
    public static double CLOUD_M2 = 0.0;


    public static void readConfiguration() {
        try {
            System.out.println(System.getProperty("user.dir"));
            Properties prop = new Properties();
            FileInputStream inputStream = new FileInputStream(FILENAME);
            prop.load(inputStream);
            SETUP_TIME = Double.parseDouble(prop.getProperty("SETUP_TIME"));
            L1 = Double.parseDouble(prop.getProperty("L1"));
            L2 = Double.parseDouble(prop.getProperty("L2"));
            CLOUDLET_M1 = Double.parseDouble(prop.getProperty("CLOUDLET_M1"));
            CLOUDLET_M2 = Double.parseDouble(prop.getProperty("CLOUDLET_M2"));
            CLOUD_M1 = Double.parseDouble(prop.getProperty("CLOUD_M1"));
            CLOUD_M2 = Double.parseDouble(prop.getProperty("CLOUD_M2"));
            S = Integer.parseInt(prop.getProperty("S"));
            N = Integer.parseInt(prop.getProperty("N"));
/*            System.out.println( "SETUP_TIME: " + SETUP_TIME + "\n" +
                                "N: " + N + "\n" +
                                "S: " + S + "\n" +
                                "L1: " + L1 + "\n" +
                                "L2: " + L2 + "\n" +
                                "CLOUDLET_M1: " + CLOUDLET_M1 + "\n" +
                                "CLOUDLET_M2: " + CLOUDLET_M2 + "\n" +
                                "CLOUD_M1: " + CLOUD_M1 + "\n" +
                                "CLOUD_M2: " + CLOUD_M2 + "\n");*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
