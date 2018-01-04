package task;

import config.AppConfiguration;

public class TaskClassTwo extends AbstractTask {

    /* Time in which task is swapped to Cloud*/
    private double swapTime;

    /* Task swapped*/
    private Boolean swapped;

    public TaskClassTwo() {
        this.swapTime = 0.0;
        this.swapped = false;
    }

    public TaskClassTwo(double arrivalTime, double serviceTime, double swapTime, Boolean swapped) {
        super(arrivalTime, serviceTime);
        this.swapTime = swapTime;
        this.swapped = swapped;
    }

    /**
     * Compute Completion Time
     */
    //TODO use random exponential setup time insted fixed SETUP_TIME
    public double getCompletionTime() {
        return this.isSwapped()? this.getSwapTime() + this.getServiceTime() + AppConfiguration.SETUP_TIME : this.getArrivalTime() + this.getServiceTime();
    }

    /**
     * Getter e Setter
     */
    public double getSwapTime() {
        return swapTime;
    }

    public void setSwapTime(double swapTime) {
        this.swapTime = swapTime;
    }

    public Boolean isSwapped() {
        return swapped;
    }

    public void setSwapped(Boolean swapped) {
        this.swapped = swapped;
    }
}
