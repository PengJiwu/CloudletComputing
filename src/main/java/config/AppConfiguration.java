package config;

import java.io.*;
import java.util.Properties;


public class AppConfiguration {

    public static final String FILENAME = "src/main/resources/application.properties";
    public static double SETUP_TIME = 0.0;
    public static int N = 0;
    public static int S = 0;
    public static double ARRIVAL_RATE_1 = 0.0;
    public static double ARRIVAL_RATE_2 = 0.0;
    public static double CLOUDLET_M1 = 0.0;
    public static double CLOUDLET_M2 = 0.0;
    public static double CLOUD_M1 = 0.0;
    public static double CLOUD_M2 = 0.0;
    public static double START = 0.0;
    public static double STOP = 0.0;
    public static int BATCH_SIZE = 256;

    public static void readConfiguration() {
        try {
            Properties prop = new Properties();
            FileInputStream inputStream = new FileInputStream(FILENAME);
            prop.load(inputStream);
            SETUP_TIME = Double.parseDouble(prop.getProperty("SETUP_TIME"));
            ARRIVAL_RATE_1 = Double.parseDouble(prop.getProperty("ARRIVAL_RATE_1"));
            ARRIVAL_RATE_2 = Double.parseDouble(prop.getProperty("ARRIVAL_RATE_2"));
            CLOUDLET_M1 = Double.parseDouble(prop.getProperty("CLOUDLET_M1"));
            CLOUDLET_M2 = Double.parseDouble(prop.getProperty("CLOUDLET_M2"));
            CLOUD_M1 = Double.parseDouble(prop.getProperty("CLOUD_M1"));
            CLOUD_M2 = Double.parseDouble(prop.getProperty("CLOUD_M2"));
            START = Double.parseDouble(prop.getProperty("START"));
            STOP = Double.parseDouble(prop.getProperty("STOP"));
            S = Integer.parseInt(prop.getProperty("S"));
            N = Integer.parseInt(prop.getProperty("N"));
            BATCH_SIZE = Integer.parseInt(prop.getProperty("BATCH_SIZE"));
/*            System.out.println( "SETUP_TIME: " + SETUP_TIME + "\n" +
                                "N: " + N + "\n" +
                                "S: " + S + "\n" +
                                "ARRIVAL_RATE_1: " + ARRIVAL_RATE_1 + "\n" +
                                "ARRIVAL_RATE_2: " + ARRIVAL_RATE_2 + "\n" +
                                "CLOUDLET_M1: " + CLOUDLET_M1 + "\n" +
                                "CLOUDLET_M2: " + CLOUDLET_M2 + "\n" +
                                "CLOUD_M1: " + CLOUD_M1 + "\n" +
                                "CLOUD_M2: " + CLOUD_M2 + "\n" +
                                "BATCH_SIZE: " + BATCH_SIZE + "\n" +
                                "START: " + START + "\n" +
                                "STOP: " + STOP + "\n");*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
