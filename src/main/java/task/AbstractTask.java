package task;

import java.util.Objects;

public abstract class AbstractTask {

    /*Task arrival time*/
    protected double arrivalTime;

    /*Task service time*/
    protected double serviceTime;

    /* Cloudlet = true ; cloud = false */
    protected Boolean cloudlet;

    public AbstractTask(){
        this.arrivalTime = 0.0;
        this.serviceTime = 0.0;
        this.cloudlet = true;
    }

    public AbstractTask(double arrivalTime, double serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.cloudlet = true;
    }

    /**
     * Abstract method to compute CompletionTime
     */
    public abstract double getCompletionTime();

    /**
     *  Getter and Setter
     */
    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public Boolean isCloudlet() {
        return cloudlet;
    }

    public void setCloudlet(Boolean cloudlet) {
        this.cloudlet = cloudlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTask that = (AbstractTask) o;
        return Double.compare(that.arrivalTime, arrivalTime) == 0 &&
                Double.compare(that.serviceTime, serviceTime) == 0 &&
                Objects.equals(cloudlet, that.cloudlet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrivalTime, serviceTime, cloudlet);
    }
}
