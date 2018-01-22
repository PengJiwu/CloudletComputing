package stat;

import config.AppConfiguration;

/**
 * BatchManager manages output statistics
 */
public class BatchManager {

    protected Performance per;
    protected PrintManager printman;
    protected int batch_size;

    protected double batchSystemUtilization;
    protected double batchSystemResponseTime;
    protected double batchSystemThroughput;
    protected double batchCloudletResponseTime;
    protected double batchCloudlet2ResponseTime;
    protected double batchCloudlet1ResponseTime;
    protected double batchCloudResponseTime;
    protected double batchCloud1ResponseTime;
    protected double batchCloud2ResponseTime;
    protected double batchCloudletPopulation;
    protected double batchCloudlet1Population;
    protected double batchCloudlet2Population;
    protected double batchCloudPopulation;
    protected double batchCloud1Population;
    protected double batchCloud2Population;

    public BatchManager(Performance p) {
        this.per = p;
        this.batch_size = AppConfiguration.BATCH_SIZE;

        this.printman = new PrintManager(this);

        resetIndexes();

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

    public void updateBatch(int index) {

        double current_time = per.clock.getCurrent();

        batchSystemUtilization += per.systemArea.service / current_time;
        batchSystemResponseTime += per.systemResponseTimeMV.getMean();
        batchSystemThroughput += index / per.clock.getCurrent();

        batchCloudletResponseTime += per.cloudletResponseTimeMV.getMean();
        batchCloudlet1ResponseTime += per.cloudlet1ResponseTimeMV.getMean();
        batchCloudlet2ResponseTime += per.cloudlet2ResponseTimeMV.getMean();
        batchCloudResponseTime += per.cloudResponseTimeMV.getMean();
        batchCloud1ResponseTime += per.cloud1ResponseTimeMV.getMean();
        batchCloud2ResponseTime += per.cloud2ResponseTimeMV.getMean();

        batchCloudletPopulation += per.cloudletArea.node / current_time;
        batchCloudlet1Population += per.cloudlet1Area.node / current_time;
        batchCloudlet2Population += per.cloudlet2Area.node / current_time;
        batchCloudPopulation += per.cloudArea.node / current_time;
        batchCloud1Population += per.cloud1Area.node / current_time;
        batchCloud2Population += per.cloud2Area.node / current_time;

        if (index % AppConfiguration.BATCH_SIZE == 0){
            printman.writeFiles();
            resetIndexes();
        }

    }
}
