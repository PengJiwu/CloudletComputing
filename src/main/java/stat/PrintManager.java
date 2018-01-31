package stat;

import config.AppConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class PrintManager {

    protected BatchManager batch;

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

    public PrintManager(BatchManager b) {
        batch = b;

        initWriters();
    }

    private static void makeDir(String dir) {
        File directory = new File(String.valueOf(dir));
        if (! directory.exists()){
            directory.mkdir();
        }
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

    public void writeFiles() {
        //systemUtilizationWriter.println(batch.batchSystemUtilization / AppConfiguration.BATCH_SIZE);
        //systemResponseTimeWriter.println(batch.batchSystemResponseTime / AppConfiguration.BATCH_SIZE);
        systemThroughputWriter.println(batch.batchSystemThroughput/ AppConfiguration.BATCH_SIZE );

        //
        //cloudletResponseTimeWriter.println(batch.batchCloudletResponseTime / AppConfiguration.BATCH_SIZE);
        //cloudlet1ResponseTimeWriter.println(batch.batchCloudlet1ResponseTime / AppConfiguration.BATCH_SIZE);
        //cloudlet2ResponseTimeWriter.println(batch.batchCloudlet2ResponseTime / AppConfiguration.BATCH_SIZE);
        //cloudResponseTimeWriter.println(batch.batchCloudResponseTime / AppConfiguration.BATCH_SIZE);
        //cloud1ResponseTimeWriter.println(batch.batchCloud1ResponseTime / AppConfiguration.BATCH_SIZE);
        //cloud2ResponseTimeWriter.println(batch.batchCloud2ResponseTime / AppConfiguration.BATCH_SIZE);
        //
        //cloudletPopulationWriter.println(batch.batchCloudletPopulation / AppConfiguration.BATCH_SIZE);
        //cloudlet1PopulationWriter.println(batch.batchCloudlet1Population / AppConfiguration.BATCH_SIZE);
        //cloudlet2PopulationWriter.println(batch.batchCloudlet2Population / AppConfiguration.BATCH_SIZE);
        //cloudPopulationWriter.println(batch.batchCloudPopulation / AppConfiguration.BATCH_SIZE);
        //cloud1PopulationWriter.println(batch.batchCloud1Population / AppConfiguration.BATCH_SIZE);
        //cloud2PopulationWriter.println(batch.batchCloud2Population / AppConfiguration.BATCH_SIZE);
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
}
