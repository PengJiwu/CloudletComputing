package task;

public abstract class AbstractTask {

    /*Task arrival time*/
    private Double arrivalTime;

    /*Task service time*/
    private Double serviceTime;

    public AbstractTask(){
        this.arrivalTime = 0.0;
        this.serviceTime = 0.0;
    }

    public AbstractTask(Double arrivalTime, Double serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    /**
     * Abstract method to compute CompletionTime
     */
    public abstract Double getCompletionTime();

    /**
     *  Getter and Setter
     */
    public Double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Double serviceTime) {
        this.serviceTime = serviceTime;
    }


    public Double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
