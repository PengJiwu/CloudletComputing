package core;

import config.AppConfiguration;

public class StartSimulation {
    public static void main(String[] args) {
        AppConfiguration.readConfiguration();
        System.exit(0);
    }
}
