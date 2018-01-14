package stat;

import cloudlet.Controller;
import config.AppConfiguration;
import utils.Clock;

import java.io.*;
import java.text.DecimalFormat;


public class Performance {


    public static Area systemArea;
    public static Area cloudletArea;
    public static Area cloudlet1Area;
    public static Area cloudlet2Area;
    public static Area cloudArea;
    public static Area cloud1Area;
    public static Area cloud2Area;

    public Clock clock;
    public Controller controller;
    public int n1, n2, cloud_n1, cloud_n2;
    public int number = 0;

    public double cloudletResponseTime;
    public double cloudlet1ResponseTime;
    public double cloudlet2ResponseTime;
    private double cloud1ResponseTime;
    private double cloud2ResponseTime;
    private double cloudResponseTime;
    private double systemResponseTime;
    private double batchSystemUtilization;
    private double batchSystemResponseTime;
    private double batchSystemThroughput;
    private double batchCloudletResponseTime;
    private double batchCloudlet2ResponseTime;
    private double batchCloudlet1ResponseTime;
    private double batchCloudResponseTime;
    private double batchCloud1ResponseTime;
    private double batchCloud2ResponseTime;
    private double batchCloudletPopulation;
    private double batchCloudlet1Population;
    private double batchCloudlet2Population;
    private double batchCloudPopulation;
    private double batchCloud1Population;
    private double batchCloud2Population;
    private PrintWriter systemUtilizationWriter;
    private PrintWriter systemThroughputWriter;
    private PrintWriter systemResponseTimeWriter;
    private PrintWriter cloudletResponseTimeWriter;
    private PrintWriter cloudlet1ResponseTimeWriter;
    private PrintWriter cloudlet2ResponseTimeWriter;
    private PrintWriter cloudResponseTimeWriter;
    private PrintWriter cloud1ResponseTimeWriter;
    private PrintWriter cloud2ResponseTimeWriter;
    private PrintWriter cloudletPopulationWriter;
    private PrintWriter cloudlet1PopulationWriter;
    private PrintWriter cloudlet2PopulationWriter;
    private PrintWriter cloudPopulationWriter;
    private PrintWriter cloud1PopulationWriter;
    private PrintWriter cloud2PopulationWriter;

    public Performance(Controller controller) {

        this.controller = controller;
        clock = Clock.getInstance();
        systemArea = new Area();
        cloudletArea = new Area();
        cloudlet1Area = new Area();
        cloudlet2Area = new Area();
        cloudArea = new Area();
        cloud1Area = new Area();
        cloud2Area = new Area();
        n1 = 0;
        n2 = 0;
        cloud_n1 = 0;
        cloud_n2 = 0;
        cloudletResponseTime = 0.0;
        cloudlet1ResponseTime = 0.0;
        cloudlet2ResponseTime = 0.0;
        cloudResponseTime = 0.0;
        cloud1ResponseTime = 0.0;
        cloud2ResponseTime = 0.0;
        systemResponseTime = 0.0;
        resetIndexes();
        initWriters();

    }

    private void initWriters() {

        try {
            systemUtilizationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            systemThroughputWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            systemResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudletResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudlet1ResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudlet2ResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloud1ResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloud2ResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloud2ResponseTimeWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudletPopulationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudlet1PopulationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudlet2PopulationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloudPopulationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloud1PopulationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
            cloud2PopulationWriter = new PrintWriter(new BufferedWriter(new FileWriter("")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void closeWriters(){
        systemUtilizationWriter.close();
        systemResponseTimeWriter.close();
        systemThroughputWriter.close();

        cloudletResponseTimeWriter.close();
        cloudlet1ResponseTimeWriter.close();
        cloudlet2ResponseTimeWriter.close();
        cloudResponseTimeWriter.close();
        cloud1ResponseTimeWriter.close();
        cloud2ResponseTimeWriter.close();

        cloudletPopulationWriter.close();
        cloudlet1PopulationWriter.close();
        cloudlet2PopulationWriter.close();
        cloudPopulationWriter.close();
        cloud1PopulationWriter.close();
        cloud2PopulationWriter.close();
    }

//

    /**
     * creates file for ResponseTime and Population for single class and global for cloudlet and cloud
     *
     */
    public static void createFile() {
        String path;
        File directory;

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output"));
        if (! directory.exists()){
            directory.mkdir();
        }

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output/cloudletResponseTime"));
        if (! directory.exists()){
            directory.mkdir();
        }

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output/cloudlet1ResponseTime"));
        if (! directory.exists()){
            directory.mkdir();
        }

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output/cloudlet2ResponseTime"));
        if (! directory.exists()){
            directory.mkdir();
        }

    }

    public void updateArea() {

        n1 = controller.getCloudletService().getN1();
        n2 = controller.getCloudletService().getN2();
        cloud_n1 = controller.getCloudService().getN1();
        cloud_n2 = controller.getCloudService().getN2();

        number = n1 + n2 + cloud_n1 + cloud_n2;


        if (number > 0)  {                               /* update system integrals */
            systemArea.service += (clock.getNext() - clock.getCurrent());
        }
        if (n1 + n2 > 0)  {                              /* update cloudlet integrals */
            cloudletArea.node += (clock.getNext() - clock.getCurrent())*(n1 + n2);
            cloudletArea.service += (clock.getNext() - clock.getCurrent());
            cloudlet1Area.node += (clock.getNext() - clock.getCurrent())*n1;
            cloudlet2Area.node += (clock.getNext() - clock.getCurrent())*n2;
        }
        if (number - (n1 + n2) > 0)  {                   /* update cloud integrals */
            cloudArea.node += (clock.getNext() - clock.getCurrent())*(number - (n1 + n2));
            cloudArea.service += (clock.getNext() - clock.getCurrent());
            cloud1Area.node += (clock.getNext() - clock.getCurrent())*cloud_n1;
            cloud2Area.node += (clock.getNext() - clock.getCurrent())*cloud_n2;
        }
    }

    public void handleCloudletCompletion(boolean classOne,double currentResponseTime) {
        double delta = 0.0;
        if (classOne){
            delta = clock.getCurrent() - controller.getCloudletService().getLastCompletionClassOne();
            cloudlet1ResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloudlet1ResponseTime));
        }
        else{
            delta = clock.getCurrent() - controller.getCloudletService().getLastCompletionClassTwo();	/* update response time with Welford's Sample Path Algorithm */
            cloudlet2ResponseTime +=  (delta/clock.getCurrent()*(currentResponseTime - cloudlet2ResponseTime));
        }
        delta = clock.getCurrent() - controller.getCloudletService().getLastCompletion();
        cloudletResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloudletResponseTime));
        handleCompletion(delta,currentResponseTime);
    }

    public void handleCloudCompletion(boolean classOne, double currentResponseTime) {
        double delta = 0.0;
        if (classOne){
            delta = clock.getCurrent() - controller.getCloudService().getLastCompletionClassOne();
            cloud1ResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloud1ResponseTime));
        }
        else{
            delta = clock.getCurrent() - controller.getCloudService().getLastCompletionClassTwo();	/* update response time with Welford's Sample Path Algorithm */
            cloud2ResponseTime +=  (delta/clock.getCurrent()*(currentResponseTime - cloud2ResponseTime));
        }
        delta = clock.getCurrent() - controller.getCloudService().getLastCompletion();
        cloudResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloudResponseTime));
        handleCompletion(delta,currentResponseTime);
    }

    public void handleCompletion(double delta,double currentResponseTime){
        systemResponseTime += (delta/clock.getCurrent()*(currentResponseTime - systemResponseTime));	/* update response time with Welford's Sample Path Algorithm */

/*        if(systemResponseTime < 0)
            System.out.println("ciao ");*/

        int index = controller.getCloudletService().getClassOneCompletion() +
                    controller.getCloudletService().getClassTwoCompletion() +
                    controller.getCloudService().getClassOneCompletion() +
                    controller.getCloudService().getClassTwoCompletion();

        batchSystemUtilization += systemArea.service / clock.getCurrent();
        batchSystemResponseTime += systemResponseTime;
        batchSystemThroughput += index / clock.getCurrent();

        batchCloudletResponseTime += cloudletResponseTime;
        batchCloudlet1ResponseTime += cloudlet1ResponseTime;
        batchCloudlet2ResponseTime += cloudlet2ResponseTime;
        batchCloudResponseTime += cloudResponseTime;
        batchCloud1ResponseTime += cloud1ResponseTime;
        batchCloud2ResponseTime += cloud2ResponseTime;

        batchCloudletPopulation += cloudletArea.node / clock.getCurrent();
        batchCloudlet1Population += cloudlet1Area.node / clock.getCurrent();
        batchCloudlet2Population += cloudlet2Area.node / clock.getCurrent();
        batchCloudPopulation += cloudArea.node / clock.getCurrent();
        batchCloud1Population += cloud1Area.node / clock.getCurrent();
        batchCloud2Population += cloud2Area.node / clock.getCurrent();

        if (index % AppConfiguration.BATCH_SIZE == 0){
            writeFiles();
            resetIndexes();
        }
    }

    private void writeFiles() {
        systemUtilizationWriter.println(batchSystemUtilization / AppConfiguration.BATCH_SIZE);
        systemResponseTimeWriter.println(batchSystemResponseTime / AppConfiguration.BATCH_SIZE);
        systemThroughputWriter.println(batchSystemThroughput / AppConfiguration.BATCH_SIZE);

        cloudletResponseTimeWriter.println(batchCloudletResponseTime / AppConfiguration.BATCH_SIZE);
        cloudlet1ResponseTimeWriter.println(batchCloudlet1ResponseTime / AppConfiguration.BATCH_SIZE);
        cloudlet2ResponseTimeWriter.println(batchCloudlet2ResponseTime / AppConfiguration.BATCH_SIZE);
        cloudResponseTimeWriter.println(batchCloudResponseTime / AppConfiguration.BATCH_SIZE);
        cloud1ResponseTimeWriter.println(batchCloud1ResponseTime / AppConfiguration.BATCH_SIZE);
        cloud2ResponseTimeWriter.println(batchCloud2ResponseTime / AppConfiguration.BATCH_SIZE);

        cloudletPopulationWriter.println(batchCloudletPopulation / AppConfiguration.BATCH_SIZE);
        cloudlet1PopulationWriter.println(batchCloudlet1Population / AppConfiguration.BATCH_SIZE);
        cloudlet2PopulationWriter.println(batchCloudlet2Population / AppConfiguration.BATCH_SIZE);
        cloudPopulationWriter.println(batchCloudPopulation / AppConfiguration.BATCH_SIZE);
        cloud1PopulationWriter.println(batchCloud1Population / AppConfiguration.BATCH_SIZE);
        cloud2PopulationWriter.println(batchCloud2Population / AppConfiguration.BATCH_SIZE);
    }

    protected void resetIndexes(){
        batchSystemUtilization = 0.0;
        batchSystemResponseTime = 0.0;
        batchSystemThroughput = 0.0;

        batchCloudletResponseTime = 0;
        batchCloudlet1ResponseTime = 0;
        batchCloudlet2ResponseTime = 0;
        batchCloudResponseTime = 0;
        batchCloud1ResponseTime = 0;
        batchCloud2ResponseTime = 0;

        batchCloudletPopulation = 0;
        batchCloudlet1Population = 0;
        batchCloudlet2Population = 0;
        batchCloudPopulation = 0;
        batchCloud1Population = 0;
        batchCloud2Population = 0;
    }

    public void printResults(){
        DecimalFormat f = new DecimalFormat("###0.0000");
        
        int index = controller.getCloudletService().getClassOneCompletion() +
                controller.getCloudletService().getClassTwoCompletion() +
                controller.getCloudService().getClassOneCompletion() +
                controller.getCloudService().getClassTwoCompletion();

        int cloudletIndex = controller.getCloudletService().getClassOneCompletion() + controller.getCloudletService().getClassTwoCompletion();
        int cloudIndex = controller.getCloudService().getClassOneCompletion() + controller.getCloudService().getClassTwoCompletion();

        System.out.println("\nfor " + index + " jobs");

        System.out.println("\n   system utilization ............. =   " + f.format(systemArea.service / clock.getCurrent()));
        System.out.println("   system mean response time....... =   " + f.format(systemResponseTime));
        System.out.println("   system throughput .............. =   " + f.format(index / clock.getCurrent()));

        System.out.println("\n   cloudlet utilization ........... =   " + f.format(cloudletArea.service / clock.getCurrent()));
        System.out.println("   cloudlet mean population ....... =   " + f.format(cloudletArea.node / clock.getCurrent()));
        System.out.println("   type 1.......................... =   " + f.format(cloudlet1Area.node / clock.getCurrent()));
        System.out.println("   type 2.......................... =   " + f.format(cloudlet2Area.node / clock.getCurrent()));
        System.out.println("   cloudlet mean response time..... =   " + f.format(cloudletResponseTime));
        System.out.println("   type 1.......................... =   " + f.format(cloudlet1ResponseTime));
        System.out.println("   type 2.......................... =   " + f.format(cloudlet2ResponseTime));
        System.out.println("   cloudlet throughput ............ =   " + f.format(cloudletIndex /clock.getCurrent()));

        System.out.println("\n   cloud utilization .............. =   " + f.format(cloudArea.service / clock.getCurrent()));
        System.out.println("   cloud mean population .......... =   " + f.format(cloudArea.node / clock.getCurrent()));
        System.out.println("   type 1.......................... =   " + f.format(cloud1Area.node / clock.getCurrent()));
        System.out.println("   type 2.......................... =   " + f.format(cloud2Area.node / clock.getCurrent()));
        System.out.println("   cloud mean response time........ =   " + f.format(cloudResponseTime));
        System.out.println("   type 1.......................... =   " + f.format(cloud1ResponseTime));
        System.out.println("   type 2.......................... =   " + f.format(cloud2ResponseTime));
        System.out.println("   cloud throughput ............... =   " + f.format(cloudIndex / clock.getCurrent()));
    }
}
