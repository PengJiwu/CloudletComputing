package stat;

import cloudlet.Controller;
import config.AppConfiguration;
import task.AbstractTask;
import task.TaskClassOne;
import task.TaskClassTwo;
import utils.Clock;

import java.text.DecimalFormat;


public class Performance {

    private BatchManager batchman;
    private ConfidenceIntervalManager ciman;


    protected static Area systemArea;
    protected static Area cloudletArea;
    protected static Area cloudlet1Area;
    protected static Area cloudlet2Area;
    protected static Area cloudArea;
    protected static Area cloud1Area;
    protected static Area cloud2Area;

    protected Clock clock;
    protected Controller controller;
    protected int n1, n2, cloud_n1, cloud_n2;
    protected int number = 0;


    protected double percentage2Preemption;
    protected int totalClassTwoPreemption;
    protected int totalClassTwoAssigned;

    /**
     * Mean value variables
     */
    protected SimpleMeanValue cloudletResponseTimeSMV;
    protected SimpleMeanValue cloudlet1ResponseTimeSMV;
    protected SimpleMeanValue cloudlet2ResponseTimeSMV;

    protected SimpleMeanValue cloudResponseTimeSMV;
    protected SimpleMeanValue cloud1ResponseTimeSMV;
    protected SimpleMeanValue cloud2ResponseTimeSMV;

    protected SimpleMeanValue systemResponseTimeSMV;
    protected SimpleMeanValue system1ResponseTimeSMV;
    protected SimpleMeanValue system2ResponseTimeSMV;

    protected SimpleMeanValue class2PreemptedResponseTimeSMV;

    private static double minResponseTime = Double.MAX_VALUE;
    private static int sGlobal = 0;
    private static double cloudletMinResponseTime = Double.MAX_VALUE;
    private static int sCloudlet = 0;
    private static double cloudMinResponseTime = Double.MAX_VALUE;
    private static int sCloud = 0;



    public Performance(Controller c) {

        controller = c;
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

        percentage2Preemption = 0.0;

        initMeanValues();
        batchman = new BatchManager(this);
        ciman = new ConfidenceIntervalManager(batchman,this);

    }

    private void initMeanValues() {
        cloudlet1ResponseTimeSMV = new SimpleMeanValue();
        cloudlet2ResponseTimeSMV = new SimpleMeanValue();
        cloudletResponseTimeSMV = new SimpleMeanValue();

        cloud1ResponseTimeSMV = new SimpleMeanValue();
        cloud2ResponseTimeSMV = new SimpleMeanValue();
        cloudResponseTimeSMV = new SimpleMeanValue();

        systemResponseTimeSMV = new SimpleMeanValue();

        system1ResponseTimeSMV = new SimpleMeanValue();
        system2ResponseTimeSMV = new SimpleMeanValue();

        class2PreemptedResponseTimeSMV = new SimpleMeanValue();
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
            // total population of jobs in cloudlet
            cloudletArea.node += (clock.getNext() - clock.getCurrent())*(n1 + n2);
            // total service time for a general job
            cloudletArea.service += (clock.getNext() - clock.getCurrent());

            // total population of jobs of type 1 in cloudlet
            cloudlet1Area.node += (clock.getNext() - clock.getCurrent())*n1;
            // total population of jobs of type 2 in cloudlet
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


    public void handleCloudletCompletion(AbstractTask task) {
        double currentResponseTime = task.getCompletionTime()-task.getArrivalTime();

        boolean classOne = (task instanceof TaskClassOne);

        if (classOne){
            cloudlet1ResponseTimeSMV.addElement(currentResponseTime);
        }
        else{
            cloudlet2ResponseTimeSMV.addElement(currentResponseTime);
        }

        cloudletResponseTimeSMV.addElement(currentResponseTime);
        handleCompletion(currentResponseTime,task);

    }

    public void handleCloudCompletion(AbstractTask task) {
        double currentResponseTime = task.getCompletionTime()-task.getArrivalTime();
        boolean classOne = (task instanceof TaskClassOne);


        if (classOne){
            // class One completion on cloud
            cloud1ResponseTimeSMV.addElement(currentResponseTime);
        }
        else{
            // class Two completion on cloud
            cloud2ResponseTimeSMV.addElement(currentResponseTime);

            TaskClassTwo t2 = (TaskClassTwo) task;
            if (t2.isSwapped()) {
                // update statistics for preempted task
                class2PreemptedResponseTimeSMV.addElement(currentResponseTime);
            }

        }
        cloudResponseTimeSMV.addElement(currentResponseTime);
        handleCompletion(currentResponseTime,task);
    }

    protected void handleCompletion(double currentResponseTime,AbstractTask task){

        /* update response time with Welford's Sample Path Algorithm */
        systemResponseTimeSMV.addElement(currentResponseTime);

        boolean isTaskOne = (task instanceof TaskClassOne);
        if (isTaskOne) {
            system1ResponseTimeSMV.addElement(currentResponseTime);
        }
        else {
            system2ResponseTimeSMV.addElement(currentResponseTime);
        }


        int index = controller.getCloudletService().getClassOneCompletion() +
                    controller.getCloudletService().getClassTwoCompletion() +
                    controller.getCloudService().getClassOneCompletion() +
                    controller.getCloudService().getClassTwoCompletion();

        batchman.updateBatch(index);

    }

    public void closeWriters() {
        batchman.printman.closeWriters();
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
        totalClassTwoPreemption = controller.getCloudletService().getTotalClassTwoPreemption();
        totalClassTwoAssigned = controller.getCloudletService().getTotalClassTwoAssigned();

        System.out.println("\n\tfor " + index + " jobs");

        System.out.println("\n\tsystem utilization ...................... =   " + f.format(systemArea.service / clock.getCurrent()));
        System.out.println("\tsystem mean response time................ =   " + f.format(systemResponseTimeSMV.getMean())+ " s");
        System.out.println("\ttype 1 mean response time................ =   " + f.format(system1ResponseTimeSMV.getMean())+ " s");
        System.out.println("\ttype 2 mean response time................ =   " + f.format(system2ResponseTimeSMV.getMean())+ " s");
        System.out.println("\tsystem throughput ....................... =   " + f.format(index / clock.getCurrent()));
        System.out.println("\ttype 1 throughput ....................... =   "
                + f.format((cloudletClassOneCompletion+cloudClassOneCompletion) / clock.getCurrent()));
        System.out.println("\ttype 2 throughput ....................... =   "
                + f.format((cloudletClassTwoCompletion+cloudClassTwoCompletion) / clock.getCurrent()));

        System.out.println("\n\tcloudlet utilization .................... =   " + f.format(cloudletArea.service / clock.getCurrent()));
        System.out.println("\tcloudlet mean population ................ =   " + f.format(cloudletArea.node / clock.getCurrent()));
        System.out.println("\ttype 1................................... =   " + f.format(cloudlet1Area.node / clock.getCurrent()));
        System.out.println("\ttype 2................................... =   " + f.format(cloudlet2Area.node / clock.getCurrent()));
        System.out.println("\tcloudlet mean response time.............. =   " + f.format(cloudletResponseTimeSMV.getMean()) + " s");
        System.out.println("\ttype 1................................... =   " + f.format(cloudlet1ResponseTimeSMV.getMean())+ " s");
        System.out.println("\ttype 2................................... =   " + f.format(cloudlet2ResponseTimeSMV.getMean())+ " s");
        System.out.println("\tcloudlet throughput ..................... =   " + f.format(cloudletIndex /clock.getCurrent()));

        System.out.println("\n\tcloud utilization ....................... =   " + f.format(cloudArea.service / clock.getCurrent()));
        System.out.println("\tcloud mean population ................... =   " + f.format(cloudArea.node / clock.getCurrent()));
        System.out.println("\ttype 1................................... =   " + f.format(cloud1Area.node / clock.getCurrent()));
        System.out.println("\ttype 2................................... =   " + f.format(cloud2Area.node / clock.getCurrent()));
        System.out.println("\tcloud mean response time................. =   " + f.format(cloudResponseTimeSMV.getMean())+ " s");
        System.out.println("\ttype 1................................... =   " + f.format(cloud1ResponseTimeSMV.getMean())+ " s");
        System.out.println("\ttype 2................................... =   " + f.format(cloud2ResponseTimeSMV.getMean())+ " s");
        System.out.println("\tcloud throughput ........................ =   " + f.format(cloudIndex / clock.getCurrent()));

        System.out.println("\n\teffective cloudlet throughput ........... =   " + f.format(cloudletIndex /clock.getCurrent()));
        System.out.println("\ttype 1................................... =   " + f.format(cloudletClassOneCompletion /clock.getCurrent()));
        System.out.println("\ttype 2................................... =   " + f.format(cloudletClassTwoCompletion /clock.getCurrent()));

        System.out.println("\n\tpercentage type 2 preempted ............. =   " + f.format(percentage2Preemption) +" %");
        System.out.println("\ttotal task 2 preempted .................. =   " + totalClassTwoPreemption);
        System.out.println("\tmean response time preempted ............ =   " + f.format(class2PreemptedResponseTimeSMV.getMean())+ " s");

        if (AppConfiguration.TEST_S){
            if (systemResponseTimeSMV.getMean() < minResponseTime){
                minResponseTime = systemResponseTimeSMV.getMean();
                sGlobal = AppConfiguration.S;
            }
            if (cloudletResponseTimeSMV.getMean() < cloudletMinResponseTime){
                cloudletMinResponseTime = cloudletResponseTimeSMV.getMean();
                sCloudlet = AppConfiguration.S;
            }if (cloudResponseTimeSMV.getMean() < cloudMinResponseTime){
                cloudMinResponseTime = cloudResponseTimeSMV.getMean();
                sCloud = AppConfiguration.S;
            }
            System.out.println("System min response time: " + minResponseTime + " s for S = " + sGlobal);
            System.out.println("Cloudlet min response time: " + cloudletMinResponseTime + " s for S = " + sCloudlet);
            System.out.println("Cloud min response time: " + cloudMinResponseTime + " s for S = " + sCloud);
        }


        ciman.setTotalCompletedJobs(index);

        ciman.printIntervalMeans();

        checkIntervals(ciman,
                    index,
                    cloudClassOneCompletion,
                    cloudletClassOneCompletion,
                    cloudletClassTwoCompletion,
                    cloudClassTwoCompletion,
                    cloudIndex,
                    cloudletIndex);
    }

    private void checkIntervals(ConfidenceIntervalManager ciman, int index, int cloudClassOneCompletion, int cloudletClassOneCompletion, int cloudletClassTwoCompletion, int cloudClassTwoCompletion, int cloudIndex, int cloudletIndex) {
        System.out.println("\n\t----- CONFIDENCE INTERVAL ESTIMATION VERIFICATION -------------------------");

        System.out.println("\n\tSystem utilization ...................... =   " +
                ciman.isInRange(systemArea.service / clock.getCurrent(),ciman.batchSystemUtilizationSMV));
        System.out.println("\tSystem mean response time................ =   " +
                ciman.isInRange(systemResponseTimeSMV.getMean(),ciman.batchSystemResponseTimeSMV));
        System.out.println("\tSystem throughput ....................... =   " +
                ciman.isInRange(index / clock.getCurrent(),ciman.batchSystemThroughputSMV));

        System.out.println("\n\tCloudlet mean response time.............. =   " +
                ciman.isInRange(cloudletResponseTimeSMV.getMean(),ciman.batchCloudletResponseTimeSMV));
        System.out.println("\tType 1................................... =   " +
                ciman.isInRange(cloudlet1ResponseTimeSMV.getMean(),ciman.batchCloudlet1ResponseTimeSMV));
        System.out.println("\tType 2................................... =   " +
                ciman.isInRange(cloudlet2ResponseTimeSMV.getMean(),ciman.batchCloudlet2ResponseTimeSMV));


        System.out.println("\n\tCloud mean response time................. =   " +
                ciman.isInRange(cloudResponseTimeSMV.getMean(),ciman.batchCloudResponseTimeSMV));
        System.out.println("\tType 1................................... =   " +
                ciman.isInRange(cloud1ResponseTimeSMV.getMean(),ciman.batchCloud1ResponseTimeSMV));
        System.out.println("\tType 2................................... =   " +
                ciman.isInRange(cloud2ResponseTimeSMV.getMean(),ciman.batchCloud2ResponseTimeSMV));


        System.out.println("\n\tCloudlet mean population ................ =   " +
                ciman.isInRange(cloudletArea.node / clock.getCurrent(),ciman.batchCloudletPopulationSMV));
        System.out.println("\tType 1................................... =   " +
                ciman.isInRange(cloudlet1Area.node / clock.getCurrent(),ciman.batchCloudlet1PopulationSMV));
        System.out.println("\tType 2................................... =   " +
                ciman.isInRange(cloudlet2Area.node / clock.getCurrent(),ciman.batchCloudlet2PopulationSMV));



        System.out.println("\n\tCloud mean population ................... =   " +
                ciman.isInRange(cloudArea.node / clock.getCurrent(),ciman.batchCloudPopulationSMV));
        System.out.println("\tType 1................................... =   " +
                ciman.isInRange(cloud1Area.node / clock.getCurrent(),ciman.batchCloud1PopulationSMV));
        System.out.println("\tType 2................................... =   " +
                ciman.isInRange(cloud2Area.node / clock.getCurrent(),ciman.batchCloud2PopulationSMV));


        System.out.println("\n\tType 1 mean response time................ =   " +
                ciman.isInRange(system1ResponseTimeSMV.getMean(),ciman.batch1SystemResponseTimeSMV));
        System.out.println("\tType 2 mean response time................ =   " +
                ciman.isInRange(system2ResponseTimeSMV.getMean(),ciman.batch2SystemResponseTimeSMV));


        System.out.println("\n\tType 1 throughput ....................... =   " +
                ciman.isInRange((cloudletClassOneCompletion+cloudClassOneCompletion) / clock.getCurrent(),ciman.batch1SystemThroughputSMV));
        System.out.println("\tType 2 throughput ....................... =   " +
                ciman.isInRange((cloudletClassTwoCompletion+cloudClassTwoCompletion) / clock.getCurrent(),ciman.batch2SystemThroughputSMV));

        System.out.println("\n\tCloudlet effective throughput type 1..... =   " +
                ciman.isInRange(cloudletClassOneCompletion /clock.getCurrent(),ciman.batch1EffectiveCloudletThroughputSMV));

        System.out.println("\tCloudlet effective throughput type 2..... =   " +
                ciman.isInRange(cloudletClassTwoCompletion /clock.getCurrent(),ciman.batch2EffectiveCloudletThroughputSMV));


        System.out.println("\n\tMean response time preempted ............ =   " +
                ciman.isInRange(class2PreemptedResponseTimeSMV.getMean(),ciman.batch2PreemptedResponseTimeSMV));


    }
}
