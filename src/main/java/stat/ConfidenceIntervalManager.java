package stat;

import library.Rvms;

import java.text.DecimalFormat;

public class ConfidenceIntervalManager {

    protected Rvms rvms;

    protected BatchManager bman;
    protected int bsize;
    protected int k;
    protected int totalJobs;

    protected double alpha = 0.05;


    protected SimpleMeanValue batchSystemResponseTimeSMV;
    protected SimpleMeanValue batchSystemThroughputSMV;

    protected SimpleMeanValue batchCloudletResponseTimeSMV;
    protected SimpleMeanValue batchCloudlet2ResponseTimeSMV;
    protected SimpleMeanValue batchCloudlet1ResponseTimeSMV;

    protected SimpleMeanValue batchCloudResponseTimeSMV;
    protected SimpleMeanValue batchCloud1ResponseTimeSMV;
    protected SimpleMeanValue batchCloud2ResponseTimeSMV;

    protected SimpleMeanValue batchSystemPopulationSMV;

    protected SimpleMeanValue batchCloudletPopulationSMV;
    protected SimpleMeanValue batchCloudlet1PopulationSMV;
    protected SimpleMeanValue batchCloudlet2PopulationSMV;

    protected SimpleMeanValue batchCloudPopulationSMV;
    protected SimpleMeanValue batchCloud1PopulationSMV;
    protected SimpleMeanValue batchCloud2PopulationSMV;

    protected SimpleMeanValue batch1SystemResponseTimeSMV;
    protected SimpleMeanValue batch2SystemResponseTimeSMV;

    protected SimpleMeanValue batch1SystemThroughputSMV;
    protected SimpleMeanValue batch2SystemThroughputSMV;

    protected SimpleMeanValue batch1EffectiveCloudletThroughputSMV;
    protected SimpleMeanValue batch2EffectiveCloudletThroughputSMV;

    protected SimpleMeanValue batch2PreemptedResponseTimeSMV;

    protected SimpleMeanValue batchEffectiveCloudletThroughputSMV;
    protected SimpleMeanValue batchCloudletUtilizationSMV;
    protected SimpleMeanValue batchCloudThroughputSMV;





    public ConfidenceIntervalManager(BatchManager b, Performance p) {
        this.rvms = new Rvms();

        this.bman = b;
        this.bsize = bman.batch_size;
        initSimpleMeanValues();

        b.setConfidenceManager(this);
    }

    private void initSimpleMeanValues() {
        batchSystemResponseTimeSMV = new SimpleMeanValue();
        batchSystemThroughputSMV = new SimpleMeanValue();

        batchCloudletResponseTimeSMV = new SimpleMeanValue();
        batchCloudlet2ResponseTimeSMV = new SimpleMeanValue();
        batchCloudlet1ResponseTimeSMV = new SimpleMeanValue();

        batchCloudResponseTimeSMV = new SimpleMeanValue();
        batchCloud1ResponseTimeSMV = new SimpleMeanValue();
        batchCloud2ResponseTimeSMV = new SimpleMeanValue();

        batchCloudletPopulationSMV = new SimpleMeanValue();
        batchCloudlet1PopulationSMV = new SimpleMeanValue();
        batchCloudlet2PopulationSMV = new SimpleMeanValue();

        batchCloudPopulationSMV = new SimpleMeanValue();
        batchCloud1PopulationSMV = new SimpleMeanValue();
        batchCloud2PopulationSMV = new SimpleMeanValue();

        batch1SystemResponseTimeSMV = new SimpleMeanValue();
        batch2SystemResponseTimeSMV = new SimpleMeanValue();

        batch1SystemThroughputSMV = new SimpleMeanValue();
        batch2SystemThroughputSMV = new SimpleMeanValue();

        batch1EffectiveCloudletThroughputSMV = new SimpleMeanValue();
        batch2EffectiveCloudletThroughputSMV = new SimpleMeanValue();

        batch2PreemptedResponseTimeSMV = new SimpleMeanValue();

        batchSystemPopulationSMV = new SimpleMeanValue();

        batchEffectiveCloudletThroughputSMV = new SimpleMeanValue();
        batchCloudletUtilizationSMV = new SimpleMeanValue();
        batchCloudThroughputSMV = new SimpleMeanValue();

    }

    public void updateInterval() {

        // updates simple mean and variance with Welford's Simple Path Algorithm

        batchSystemResponseTimeSMV.addElement(bman.batchSystemResponseTime / bsize);
        batchSystemThroughputSMV.addElement(bman.batchSystemThroughput / bsize);
        batchSystemPopulationSMV.addElement(bman.batchSystemPopulation / bsize);

        batchCloudletResponseTimeSMV.addElement(bman.batchCloudletResponseTime / bsize);
        batchCloudlet1ResponseTimeSMV.addElement(bman.batchCloudlet1ResponseTime / bsize);
        batchCloudlet2ResponseTimeSMV.addElement(bman.batchCloudlet2ResponseTime / bsize);

        batchCloudResponseTimeSMV.addElement(bman.batchCloudResponseTime / bsize);
        batchCloud1ResponseTimeSMV.addElement(bman.batchCloud1ResponseTime / bsize);
        batchCloud2ResponseTimeSMV.addElement(bman.batchCloud2ResponseTime / bsize);

        batchCloudletPopulationSMV.addElement(bman.batchCloudletPopulation / bsize);
        batchCloudlet1PopulationSMV.addElement(bman.batchCloudlet1Population / bsize);
        batchCloudlet2PopulationSMV.addElement(bman.batchCloudlet2Population / bsize);

        batchCloudPopulationSMV.addElement(bman.batchCloudPopulation / bsize);
        batchCloud1PopulationSMV.addElement(bman.batchCloud1Population / bsize);
        batchCloud2PopulationSMV.addElement(bman.batchCloud2Population / bsize);

        batch1SystemResponseTimeSMV.addElement(bman.batch1SystemResponseTime / bsize);
        batch2SystemResponseTimeSMV.addElement(bman.batch2SystemResponseTime / bsize);

        batch1SystemThroughputSMV.addElement(bman.batch1SystemThroughput / bsize);
        batch2SystemThroughputSMV.addElement(bman.batch2SystemThroughput / bsize);

        batch1EffectiveCloudletThroughputSMV.addElement(bman.batch1EffectiveCloudletThroughput / bsize);
        batch2EffectiveCloudletThroughputSMV.addElement(bman.batch2EffectiveCloudletThroughput / bsize);

        batch2PreemptedResponseTimeSMV.addElement(bman.batch2PreemptedResponseTime / bsize);

        batchEffectiveCloudletThroughputSMV.addElement(bman.batchEffectiveCloudletThroughput / bsize);
        batchCloudletUtilizationSMV.addElement(bman.batchCloudletUtilization / bsize);
        batchCloudThroughputSMV.addElement(bman.batchCloudThroughput / bsize);

    }

    public void printIntervalMeans() {
        DecimalFormat f = new DecimalFormat("###0.0000");
        String _space = "\t\t";

        System.out.println("\n\t----- CONFIDENCE INTERVAL ESTIMATION -------------------------");
        System.out.println("\n\tfor n="+totalJobs+" with (b,k) = ("+bsize+","+k+")");



        System.out.println("\tSystem mean population......................."+_space
                +f.format(batchSystemPopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchSystemPopulationSMV))+_space
                +f.format(getUpperEndPoint(batchSystemPopulationSMV))
        );
        System.out.println("\tSystem response time........................."+_space
                +f.format(batchSystemResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchSystemResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchSystemResponseTimeSMV))
        );
        System.out.println("\tSystem throughput............................"+_space
                +f.format(batchSystemThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchSystemThroughputSMV))+_space
                +f.format(getUpperEndPoint(batchSystemThroughputSMV))
        );

        System.out.println("\n\tCloudlet utilization........................."+_space
                +f.format(batchCloudletUtilizationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudletUtilizationSMV))+_space
                +f.format(getUpperEndPoint(batchCloudletUtilizationSMV))
        );
        System.out.println("\tCloudlet response time......................."+_space
                +f.format(batchCloudletResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudletResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchCloudletResponseTimeSMV))
        );
        System.out.println("\tType 1 response time........................."+_space
                +f.format(batchCloudlet1ResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudlet1ResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchCloudlet1ResponseTimeSMV))
        );
        System.out.println("\tType 2 response time........................."+_space
                +f.format(batchCloudlet2ResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudlet2ResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchCloudlet2ResponseTimeSMV))
        );

        System.out.println("\tCloud response time.........................."+_space
                +f.format(batchCloudResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchCloudResponseTimeSMV))
        );
        System.out.println("\tType 1 response time........................."+_space
                +f.format(batchCloud1ResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloud1ResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchCloud1ResponseTimeSMV))
        );
        System.out.println("\tType 2 response time........................."+_space
                +f.format(batchCloud2ResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloud2ResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batchCloud2ResponseTimeSMV))
        );

        System.out.println("\n\tCloudlet population.........................."+_space
                +f.format(batchCloudletPopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudletPopulationSMV))+_space
                +f.format(getUpperEndPoint(batchCloudletPopulationSMV))
        );
        System.out.println("\tType 1 population............................"+_space
                +f.format(batchCloudlet1PopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudlet1PopulationSMV))+_space
                +f.format(getUpperEndPoint(batchCloudlet1PopulationSMV))
        );
        System.out.println("\tType 2 population............................"+_space
                +f.format(batchCloudlet2PopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudlet2PopulationSMV))+_space
                +f.format(getUpperEndPoint(batchCloudlet2PopulationSMV))
        );

        System.out.println("\n\tCloud population............................."+_space
                +f.format(batchCloudPopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudPopulationSMV))+_space
                +f.format(getUpperEndPoint(batchCloudPopulationSMV))
        );
        System.out.println("\tType 1 population............................"+_space
                +f.format(batchCloud1PopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloud1PopulationSMV))+_space
                +f.format(getUpperEndPoint(batchCloud1PopulationSMV))
        );
        System.out.println("\tType 2 population............................"+_space
                +f.format(batchCloud2PopulationSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloud2PopulationSMV))+_space
                +f.format(getUpperEndPoint(batchCloud2PopulationSMV))
        );


        System.out.println("\n\tSystem response time type 1.................."+_space
                +f.format(batch1SystemResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch1SystemResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batch1SystemResponseTimeSMV))
        );
        System.out.println("\tSystem response time type 2.................."+_space
                +f.format(batch2SystemResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch2SystemResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batch2SystemResponseTimeSMV))
        );

        System.out.println("\n\tSystem throughput type 1....................."+_space
                +f.format(batch1SystemThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch1SystemThroughputSMV))+_space
                +f.format(getUpperEndPoint(batch1SystemThroughputSMV))
        );
        System.out.println("\tSystem throughput type 2....................."+_space
                +f.format(batch2SystemThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch2SystemThroughputSMV))+_space
                +f.format(getUpperEndPoint(batch2SystemThroughputSMV))
        );

        System.out.println("\n\tCloudlet effective throughput ..............."+_space
                +f.format(batchEffectiveCloudletThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchEffectiveCloudletThroughputSMV))+_space
                +f.format(getUpperEndPoint(batchEffectiveCloudletThroughputSMV))
        );
        System.out.println("\tCloudlet effective throughput type 1........."+_space
                +f.format(batch1EffectiveCloudletThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch1EffectiveCloudletThroughputSMV))+_space
                +f.format(getUpperEndPoint(batch1EffectiveCloudletThroughputSMV))
        );
        System.out.println("\tCloudlet effective throughput type 2........."+_space
                +f.format(batch2EffectiveCloudletThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch2EffectiveCloudletThroughputSMV))+_space
                +f.format(getUpperEndPoint(batch2EffectiveCloudletThroughputSMV))
        );
        System.out.println("\tCloud throughput ............................"+_space
                +f.format(batchCloudThroughputSMV.getMean())+_space
                +f.format(getLowerEndPoint(batchCloudThroughputSMV))+_space
                +f.format(getUpperEndPoint(batchCloudThroughputSMV))
        );


        System.out.println("\n\tPreempted response time......................"+_space
                +f.format(batch2PreemptedResponseTimeSMV.getMean())+_space
                +f.format(getLowerEndPoint(batch2PreemptedResponseTimeSMV))+_space
                +f.format(getUpperEndPoint(batch2PreemptedResponseTimeSMV))
        );
    }

    protected double getLowerEndPoint(SimpleMeanValue mv) {
        double t = rvms.idfStudent(k-1,1-alpha/2);
        double stddev = Math.sqrt(mv.getVariance());
        double ep = (t * stddev) / (Math.sqrt(k-1));
        return -ep;
    }

    protected double getUpperEndPoint(SimpleMeanValue mv) {
        double t = rvms.idfStudent(k-1,1-alpha/2);
        double stddev = Math.sqrt(mv.getVariance());
        double ep = (t * stddev) / (Math.sqrt(k-1));
        return ep;
    }

    public void setTotalCompletedJobs(int totalCompletedJobs) {
        this.totalJobs = totalCompletedJobs;
        this.k = totalCompletedJobs / bsize;
    }

    protected String isInRange(double original, SimpleMeanValue mv) {
        DecimalFormat f = new DecimalFormat("###0.0000");

        double lowerEP = getLowerEndPoint(mv);
        double upperEP = getUpperEndPoint(mv);

        boolean result = mv.getMean()+lowerEP <= original && original <= mv.getMean()+upperEP;

        return "["+result+"] \tvalue "+f.format(original)+" \tin range ["
                    + f.format(mv.getMean()+lowerEP) +" - "
                    + f.format(mv.getMean()+upperEP)+"]";
    }
}


