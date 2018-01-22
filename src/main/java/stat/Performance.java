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

    private double percentage2Preemption;
    private int totalClassTwoPreempion;
    private int totalClassTwoAssigned;

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
        percentage2Preemption = 0.0;

        resetIndexes();
        initWriters();

    }

    private static PrintWriter createPrintWriter(String filePath) {
        File aFile = new File(filePath);
        PrintWriter aWriter = null;
        try {
            aFile.createNewFile();
            aWriter = new PrintWriter(aFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return aWriter;
    }
    private void initWriters() {

        createFile();

        // general system stats
        systemUtilizationWriter = createPrintWriter("output/system/utilization.txt");
        systemThroughputWriter = createPrintWriter("output/system/throughput.txt");
        systemResponseTimeWriter = createPrintWriter("output/system/response_time.txt");

        // cloudlet stats
        cloudletResponseTimeWriter = createPrintWriter("output/cloudlet/response_time.txt");
        cloudlet1ResponseTimeWriter = createPrintWriter("output/cloudlet/response_time_class_one.txt");
        cloudlet2ResponseTimeWriter = createPrintWriter("output/cloudlet/response_time_class_two.txt");

        cloudletPopulationWriter = createPrintWriter("output/cloudlet/population.txt");
        cloudlet1PopulationWriter = createPrintWriter("output/cloudlet/population_class_one.txt");
        cloudlet2PopulationWriter = createPrintWriter("output/cloudlet/population_class_two.txt");

        // cloud stats
        cloudResponseTimeWriter = createPrintWriter("output/cloud/response_time.txt");
        cloud1ResponseTimeWriter = createPrintWriter("output/cloud/response_time_class_one.txt");
        cloud2ResponseTimeWriter = createPrintWriter("output/cloud/response_time_class_two.txt");

        cloudPopulationWriter = createPrintWriter("output/cloud/population.txt");
        cloud1PopulationWriter = createPrintWriter("output/cloud/population_class_one.txt");
        cloud2PopulationWriter = createPrintWriter("output/cloud/population_class_two.txt");
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
    private static void makeDir(String dir) {
        File directory = new File(String.valueOf(dir));
        if (! directory.exists()){
            directory.mkdir();
        }
    }

    /**
     * creates file for ResponseTime and Population for single class and global for cloudlet and cloud
     *
     */
    protected static void createFile() {
        String path;

        path = System.getProperty("user.dir");

        String outputDir = path+"/output";
        makeDir(outputDir);

        String dir = outputDir+"/system";
        makeDir(dir);

        dir = outputDir+"/cloudlet";
        makeDir(dir);

        dir = outputDir+"/cloud";
        makeDir(dir);

    }

    public void updateArea() {
        // statistics for population indexes

        n1 = controller.getCloudletService().getN1();
        n2 = controller.getCloudletService().getN2();
        cloud_n1 = controller.getCloudService().getN1();
        cloud_n2 = controller.getCloudService().getN2();

        number = n1 + n2 + cloud_n1 + cloud_n2;

        /* update system integrals */
        if (number > 0)  {
            systemArea.service += (clock.getNext() - clock.getCurrent());
        }
        /* update cloudlet integrals */
        if (n1 + n2 > 0)  {
            cloudletArea.node += (clock.getNext() - clock.getCurrent())*(n1 + n2);
            cloudletArea.service += (clock.getNext() - clock.getCurrent());
            cloudlet1Area.node += (clock.getNext() - clock.getCurrent())*n1;
            cloudlet2Area.node += (clock.getNext() - clock.getCurrent())*n2;
        }
        /* update cloud integrals */
        if (cloud_n1 + cloud_n2 > 0)  {
            cloudArea.node += (clock.getNext() - clock.getCurrent())*(number - (n1 + n2));
            cloudArea.service += (clock.getNext() - clock.getCurrent());
            cloud1Area.node += (clock.getNext() - clock.getCurrent())*cloud_n1;
            cloud2Area.node += (clock.getNext() - clock.getCurrent())*cloud_n2;
        }
    }


    public void handleCloudletCompletion(boolean classOne,double currentResponseTime) {

        // x_i is currentResponseTime
        double delta = 0.0;
        if (classOne){
            // delta_i = t_i - t_i-1
            // getCurrent is t_i
            // getLastCompletionClassOne() is t_i-1

            /* update response time with Welford's Sample Path Algorithm */
            delta = clock.getCurrent() - controller.getCloudletService().getLastCompletionClassOne();
            // x_i = x_i-1 + (delta / t_i)*(x_i - x_i-1)
            cloudlet1ResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloudlet1ResponseTime));
        }
        else{
            /* update response time with Welford's Sample Path Algorithm */
            delta = clock.getCurrent() - controller.getCloudletService().getLastCompletionClassTwo();	/* update response time with Welford's Sample Path Algorithm */
            cloudlet2ResponseTime +=  (delta/clock.getCurrent()*(currentResponseTime - cloudlet2ResponseTime));
        }
        // global indexes
        delta = clock.getCurrent() - controller.getCloudletService().getLastCompletion();
        cloudletResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloudletResponseTime));
        handleCompletion(delta,currentResponseTime);
    }

    public void handleCloudCompletion(boolean classOne, double currentResponseTime) {
        double delta = 0.0;
        if (classOne){
            /* update response time with Welford's Sample Path Algorithm */
            delta = clock.getCurrent() - controller.getCloudService().getLastCompletionClassOne();
            cloud1ResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloud1ResponseTime));
        }
        else{
            /* update response time with Welford's Sample Path Algorithm */
            delta = clock.getCurrent() - controller.getCloudService().getLastCompletionClassTwo();
            cloud2ResponseTime +=  (delta/clock.getCurrent()*(currentResponseTime - cloud2ResponseTime));
        }
        delta = clock.getCurrent() - controller.getCloudService().getLastCompletion();
        cloudResponseTime += (delta/clock.getCurrent()*(currentResponseTime - cloudResponseTime));
        handleCompletion(delta,currentResponseTime);
    }

    protected void handleCompletion(double delta,double currentResponseTime){
        /* update response time with Welford's Sample Path Algorithm */
        systemResponseTime += (delta/clock.getCurrent()*(currentResponseTime - systemResponseTime));

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

    protected void writeFiles() {
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

        int cloudletClassOneCompletion = controller.getCloudletService().getClassOneCompletion();
        int cloudletClassTwoCompletion = controller.getCloudletService().getClassTwoCompletion();
        int cloudletIndex = cloudletClassOneCompletion + cloudletClassTwoCompletion;


        int cloudClassOneCompletion = controller.getCloudService().getClassOneCompletion();
        int cloudClassTwoCompletion = controller.getCloudService().getClassTwoCompletion();
        int cloudIndex = cloudClassOneCompletion + cloudClassTwoCompletion;


        percentage2Preemption = controller.getCloudletService().getPercentage2Preemption() * 100.0;
        totalClassTwoPreempion = controller.getCloudletService().getTotalClassTwoPreempion();
        totalClassTwoAssigned = controller.getCloudletService().getTotalClassTwoAssigned();

        System.out.println("\n\tfor " + index + " jobs");

        System.out.println("\n\tsystem utilization ............. =   " + f.format(systemArea.service / clock.getCurrent()));
        System.out.println("\tsystem mean response time....... =   " + f.format(systemResponseTime));
        System.out.println("\tsystem throughput .............. =   " + f.format(index / clock.getCurrent()));

        System.out.println("\n\tcloudlet utilization ........... =   " + f.format(cloudletArea.service / clock.getCurrent()));
        System.out.println("\tcloudlet mean population ....... =   " + f.format(cloudletArea.node / clock.getCurrent()));
        System.out.println("\ttype 1.......................... =   " + f.format(cloudlet1Area.node / clock.getCurrent()));
        System.out.println("\ttype 2.......................... =   " + f.format(cloudlet2Area.node / clock.getCurrent()));
        System.out.println("\tcloudlet mean response time..... =   " + f.format(cloudletResponseTime));
        System.out.println("\ttype 1.......................... =   " + f.format(cloudlet1ResponseTime));
        System.out.println("\ttype 2.......................... =   " + f.format(cloudlet2ResponseTime));
        System.out.println("\tcloudlet throughput ............ =   " + f.format(cloudletIndex /clock.getCurrent()));

        System.out.println("\n\tcloud utilization .............. =   " + f.format(cloudArea.service / clock.getCurrent()));
        System.out.println("\tcloud mean population .......... =   " + f.format(cloudArea.node / clock.getCurrent()));
        System.out.println("\ttype 1.......................... =   " + f.format(cloud1Area.node / clock.getCurrent()));
        System.out.println("\ttype 2.......................... =   " + f.format(cloud2Area.node / clock.getCurrent()));
        System.out.println("\tcloud mean response time........ =   " + f.format(cloudResponseTime));
        System.out.println("\ttype 1.......................... =   " + f.format(cloud1ResponseTime));
        System.out.println("\ttype 2.......................... =   " + f.format(cloud2ResponseTime));
        System.out.println("\tcloud throughput ............... =   " + f.format(cloudIndex / clock.getCurrent()));

        System.out.println("\n\teffective cloudlet throughput .. =   " + f.format(cloudletIndex /clock.getCurrent()));
        System.out.println("\ttype 1.......................... =   " + f.format(cloudletClassOneCompletion /clock.getCurrent()));
        System.out.println("\ttype 2.......................... =   " + f.format(cloudletClassTwoCompletion /clock.getCurrent()));

        System.out.println("\n\tpercentage type 2 preempted .... =   " + f.format(percentage2Preemption) +" %");
        System.out.println("\ttotal task 2 preempted ......... =   " + totalClassTwoPreempion);




    }
}
