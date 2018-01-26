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
    protected WeightedMeanValue cloudletResponseTimeMV;
    protected WeightedMeanValue cloudlet1ResponseTimeMV;
    protected WeightedMeanValue cloudlet2ResponseTimeMV;

    protected WeightedMeanValue cloudResponseTimeMV;
    protected WeightedMeanValue cloud1ResponseTimeMV;
    protected WeightedMeanValue cloud2ResponseTimeMV;

    protected WeightedMeanValue systemResponseTimeMV;
    protected WeightedMeanValue system1ResponseTimeMV;
    protected WeightedMeanValue system2ResponseTimeMV;

    protected WeightedMeanValue class2PreemptedResponseTimeMV;

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
        ciman = new ConfidenceIntervalManager(batchman);

    }

    private void initMeanValues() {
        cloudlet1ResponseTimeMV = new WeightedMeanValue();
        cloudlet2ResponseTimeMV = new WeightedMeanValue();
        cloudletResponseTimeMV = new WeightedMeanValue();

        cloud1ResponseTimeMV = new WeightedMeanValue();
        cloud2ResponseTimeMV = new WeightedMeanValue();
        cloudResponseTimeMV = new WeightedMeanValue();

        systemResponseTimeMV = new WeightedMeanValue();

        system1ResponseTimeMV = new WeightedMeanValue();
        system2ResponseTimeMV = new WeightedMeanValue();

        class2PreemptedResponseTimeMV = new WeightedMeanValue();
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
        double cur_event = clock.getCurrent();
        boolean classOne = (task instanceof TaskClassOne);

        if (classOne){
            double prev_event = controller.getCloudletService().getLastCompletionClassOne();
            cloudlet1ResponseTimeMV.addElement(currentResponseTime,cur_event,prev_event);
        }
        else{
            double prev_event = controller.getCloudletService().getLastCompletionClassTwo();
            cloudlet2ResponseTimeMV.addElement(currentResponseTime,cur_event,prev_event);
        }

        double prev_event = controller.getCloudletService().getLastCompletion();
        cloudletResponseTimeMV.addElement(currentResponseTime,cur_event,prev_event);
        handleCompletion(cur_event,prev_event,currentResponseTime,task);

    }

    public void handleCloudCompletion(AbstractTask task) {
        double currentResponseTime = task.getCompletionTime()-task.getArrivalTime();
        boolean classOne = (task instanceof TaskClassOne);
        double cur_event = clock.getCurrent();

        if (classOne){
            // class One completion on cloud
            double prev_event = controller.getCloudService().getLastCompletionClassOne();
            cloud1ResponseTimeMV.addElement(currentResponseTime,cur_event,prev_event);
        }
        else{
            // class Two completion on cloud
            double prev_event = controller.getCloudService().getLastCompletionClassTwo();
            cloud2ResponseTimeMV.addElement(currentResponseTime,cur_event,prev_event);

            TaskClassTwo t2 = (TaskClassTwo) task;
            if (t2.isSwapped()) {
                // update statistics for preempted task
                double prev_preemp_event = controller.getCloudletService().getLastPreemptionTime();
                class2PreemptedResponseTimeMV.addElement(currentResponseTime,cur_event,prev_preemp_event);
            }

        }

        double prev_event = controller.getCloudService().getLastCompletion();
        cloudResponseTimeMV.addElement(currentResponseTime,cur_event,prev_event);
        handleCompletion(cur_event,prev_event,currentResponseTime,task);
    }

    protected void handleCompletion(double cur_time, double prev_time,double currentResponseTime,AbstractTask task){

        /* update response time with Welford's Sample Path Algorithm */
        systemResponseTimeMV.addElement(currentResponseTime,cur_time,prev_time);

        boolean isTaskOne = (task instanceof TaskClassOne);
        if (isTaskOne) {
            system1ResponseTimeMV.addElement(currentResponseTime,cur_time,prev_time);
        }
        else {
            system2ResponseTimeMV.addElement(currentResponseTime,cur_time,prev_time);
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
        System.out.println("\tsystem weighted mean response time....... =   " + f.format(systemResponseTimeMV.getMean())+ " s");
        System.out.println("\ttype 1 mean response time................ =   " + f.format(system1ResponseTimeMV.getMean())+ " s");
        System.out.println("\ttype 2 mean response time................ =   " + f.format(system2ResponseTimeMV.getMean())+ " s");
        System.out.println("\tsystem throughput ....................... =   " + f.format(index / clock.getCurrent()));
        System.out.println("\ttype 1 throughput ....................... =   "
                + f.format((cloudletClassOneCompletion+cloudClassOneCompletion) / clock.getCurrent()));
        System.out.println("\ttype 2 throughput ....................... =   "
                + f.format((cloudletClassTwoCompletion+cloudClassTwoCompletion) / clock.getCurrent()));

        System.out.println("\n\tcloudlet utilization .................... =   " + f.format(cloudletArea.service / clock.getCurrent()));
        System.out.println("\tcloudlet mean population ................ =   " + f.format(cloudletArea.node / clock.getCurrent()));
        System.out.println("\ttype 1................................... =   " + f.format(cloudlet1Area.node / clock.getCurrent()));
        System.out.println("\ttype 2................................... =   " + f.format(cloudlet2Area.node / clock.getCurrent()));
        System.out.println("\tcloudlet mean response time.............. =   " + f.format(cloudletResponseTimeMV.getMean()) + " s");
        System.out.println("\ttype 1................................... =   " + f.format(cloudlet1ResponseTimeMV.getMean())+ " s");
        System.out.println("\ttype 2................................... =   " + f.format(cloudlet2ResponseTimeMV.getMean())+ " s");
        System.out.println("\tcloudlet throughput ..................... =   " + f.format(cloudletIndex /clock.getCurrent()));

        System.out.println("\n\tcloud utilization ....................... =   " + f.format(cloudArea.service / clock.getCurrent()));
        System.out.println("\tcloud mean population ................... =   " + f.format(cloudArea.node / clock.getCurrent()));
        System.out.println("\ttype 1................................... =   " + f.format(cloud1Area.node / clock.getCurrent()));
        System.out.println("\ttype 2................................... =   " + f.format(cloud2Area.node / clock.getCurrent()));
        System.out.println("\tcloud mean response time................. =   " + f.format(cloudResponseTimeMV.getMean())+ " s");
        System.out.println("\ttype 1................................... =   " + f.format(cloud1ResponseTimeMV.getMean())+ " s");
        System.out.println("\ttype 2................................... =   " + f.format(cloud2ResponseTimeMV.getMean())+ " s");
        System.out.println("\tcloud throughput ........................ =   " + f.format(cloudIndex / clock.getCurrent()));

        System.out.println("\n\teffective cloudlet throughput ........... =   " + f.format(cloudletIndex /clock.getCurrent()));
        System.out.println("\ttype 1................................... =   " + f.format(cloudletClassOneCompletion /clock.getCurrent()));
        System.out.println("\ttype 2................................... =   " + f.format(cloudletClassTwoCompletion /clock.getCurrent()));

        System.out.println("\n\tpercentage type 2 preempted ............. =   " + f.format(percentage2Preemption) +" %");
        System.out.println("\ttotal task 2 preempted .................. =   " + totalClassTwoPreemption);
        System.out.println("\tmean response time preempted ............ =   " + f.format(class2PreemptedResponseTimeMV.getMean())+ " s");

        if (AppConfiguration.TEST_S){
            if (systemResponseTimeMV.getMean() < minResponseTime){
                minResponseTime = systemResponseTimeMV.getMean();
                sGlobal = AppConfiguration.S;
            }
            if (cloudletResponseTimeMV.getMean() < cloudletMinResponseTime){
                cloudletMinResponseTime = cloudletResponseTimeMV.getMean();
                sCloudlet = AppConfiguration.S;
            }if (cloudResponseTimeMV.getMean() < cloudMinResponseTime){
                cloudMinResponseTime = cloudResponseTimeMV.getMean();
                sCloud = AppConfiguration.S;
            }
            System.out.println("System min response time: " + minResponseTime + " s for S = " + sGlobal);
            System.out.println("Cloudlet min response time: " + cloudletMinResponseTime + " s for S = " + sCloudlet);
            System.out.println("Cloud min response time: " + cloudMinResponseTime + " s for S = " + sCloud);
        }


        ciman.setTotalCompletedJobs(index);

        ciman.printIntervalMeans();
    }
}
