package task;

public abstract class AbstractTask {

    /*Task arrival time*/
    protected double arrivalTime;

    /*Task service time*/
    protected double serviceTime;

    public AbstractTask(){
        this.arrivalTime = 0.0;
        this.serviceTime = 0.0;
    }

    public AbstractTask(double arrivalTime, double serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
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

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "AbstractTask{" +
                "arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
