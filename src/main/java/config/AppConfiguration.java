package config;

import java.io.*;
import java.util.Properties;


public class AppConfiguration {

    public static final String FILENAME = "src/main/resources/application.properties";
    public static Double SETUP_TIME = 0.0;
    public static int N = 0;
    public static int S = 0;
    public static Double L1 = 0.0;
    public static Double L2 = 0.0;
    public static Double CLOUDLET_M1 = 0.0;
    public static Double CLOUDLET_M2 = 0.0;
    public static Double CLOUD_M1 = 0.0;
    public static Double CLOUD_M2 = 0.0;
    public static Double CLOUDLET_ST1 = 0.0;
    public static Double CLOUDLET_ST2 = 0.0;
    public static Double CLOUD_ST1 = 0.0;
    public static Double CLOUD_ST2 = 0.0;


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
            CLOUDLET_ST1 = (double)Math.round(1/CLOUDLET_M1 * 10000d) / 10000d;
            CLOUDLET_ST2 = (double)Math.round(1/CLOUDLET_M2 * 10000d) / 10000d;
            CLOUD_ST1 = (double)Math.round(1/CLOUD_M1 * 10000d) / 10000d;
            CLOUD_ST2 = (double)Math.round(1/CLOUD_M2 * 10000d) / 10000d;
/*            System.out.println( "SETUP_TIME: " + SETUP_TIME + "\n" +
                                "N: " + N + "\n" +
                                "S: " + S + "\n" +
                                "L1: " + L1 + "\n" +
                                "L2: " + L2 + "\n" +
                                "CLOUDLET_M1: " + CLOUDLET_M1 + "\n" +
                                "CLOUDLET_M2: " + CLOUDLET_M2 + "\n" +
                                "CLOUD_M1: " + CLOUD_M1 + "\n" +
                                "CLOUD_M2: " + CLOUD_M2 + "\n" +
                                "CLOUDLET_ST1: " + CLOUDLET_ST1 + "\n" +
                                "CLOUDLET_ST2: " + CLOUDLET_ST2 + "\n" +
                                "CLOUD_ST1: " + CLOUD_ST1 + "\n" +
                                "CLOUD_ST2: " + CLOUD_ST2 + "\n");*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
