package stat;

import config.AppConfiguration;

/**
 * BatchManager manages output statistics
 */
public class BatchManager {

    protected Performance per;
    protected PrintManager printman;

    protected ConfidenceIntervalManager ciman;


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

    protected double batch1SystemResponseTime;
    protected double batch2SystemResponseTime;

    protected double batch1SystemThroughput;
    protected double batch2SystemThroughput;

    protected double batch1EffectiveCloudletThroughput;
    protected double batch2EffectiveCloudletThroughput;

    protected double batch2PreemptedResponseTime;
    protected double batch2TotalPreemptedTask;
    protected double batch2TotalClassTwoAssigned;

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

        batchCloudletResponseTime = 0.0;
        batchCloudlet1ResponseTime = 0.0;
        batchCloudlet2ResponseTime = 0.0;
        batchCloudResponseTime = 0.0;
        batchCloud1ResponseTime = 0.0;
        batchCloud2ResponseTime = 0.0;

        batchCloudletPopulation = 0.0;
        batchCloudlet1Population = 0.0;
        batchCloudlet2Population = 0.0;
        batchCloudPopulation = 0.0;
        batchCloud1Population = 0.0;
        batchCloud2Population = 0.0;

        batch1SystemResponseTime = 0.0;
        batch2SystemResponseTime = 0.0;

        batch1SystemThroughput = 0.0;
        batch2SystemThroughput = 0.0;

        batch1EffectiveCloudletThroughput = 0.0;
        batch2EffectiveCloudletThroughput = 0.0;

        batch2PreemptedResponseTime = 0.0;
        batch2TotalPreemptedTask = 0.0;
        batch2TotalClassTwoAssigned = 0.0;
    }

    public void updateBatch(int index) {

        double current_time = per.clock.getCurrent();

        batchSystemUtilization += per.systemArea.service / current_time;

        batchSystemResponseTime += per.systemResponseTimeSMV.getMean();
        batch1SystemResponseTime += per.system1ResponseTimeSMV.getMean();
        batch2SystemResponseTime += per.system2ResponseTimeSMV.getMean();

        batchSystemThroughput += index / per.clock.getCurrent();

        int cloudletClassOneCompletion = per.controller.getCloudletService().getClassOneCompletion();
        int cloudletClassTwoCompletion = per.controller.getCloudletService().getClassTwoCompletion();
        int cloudletIndex = cloudletClassOneCompletion + cloudletClassTwoCompletion;


        int cloudClassOneCompletion = per.controller.getCloudService().getClassOneCompletion();
        int cloudClassTwoCompletion = per.controller.getCloudService().getClassTwoCompletion();
        int cloudIndex = cloudClassOneCompletion + cloudClassTwoCompletion;

        batch1SystemThroughput += (cloudletClassOneCompletion+cloudClassOneCompletion) / current_time;
        batch2SystemThroughput +=(cloudletClassTwoCompletion+cloudClassTwoCompletion) / current_time;



        batchCloudletResponseTime += per.cloudletResponseTimeSMV.getMean();
        batchCloudlet1ResponseTime += per.cloudlet1ResponseTimeSMV.getMean();
        batchCloudlet2ResponseTime += per.cloudlet2ResponseTimeSMV.getMean();
        batchCloudResponseTime += per.cloudResponseTimeSMV.getMean();
        batchCloud1ResponseTime += per.cloud1ResponseTimeSMV.getMean();
        batchCloud2ResponseTime += per.cloud2ResponseTimeSMV.getMean();

        batchCloudletPopulation += per.cloudletArea.node / current_time;
        batchCloudlet1Population += per.cloudlet1Area.node / current_time;
        batchCloudlet2Population += per.cloudlet2Area.node / current_time;
        batchCloudPopulation += per.cloudArea.node / current_time;
        batchCloud1Population += per.cloud1Area.node / current_time;
        batchCloud2Population += per.cloud2Area.node / current_time;



        batch1EffectiveCloudletThroughput += cloudletClassOneCompletion / current_time;
        batch2EffectiveCloudletThroughput += cloudletClassTwoCompletion / current_time;

        batch2PreemptedResponseTime += per.class2PreemptedResponseTimeSMV.getMean();
        batch2TotalPreemptedTask += per.controller.getCloudletService().getTotalClassTwoPreemption();
        batch2TotalClassTwoAssigned += per.controller.getCloudletService().getTotalClassTwoAssigned();

        updateIntervalsAndReset(index);
    }

    private void updateIntervalsAndReset(int index) {

        if (index % AppConfiguration.BATCH_SIZE == 0){
            printman.writeFiles();
            ciman.updateInterval();
            resetIndexes();
        }
    }

    public void setConfidenceManager(ConfidenceIntervalManager ciman) {
        this.ciman = ciman;
    }
}
