package task;

import config.AppConfiguration;

public class TaskClassTwo extends AbstractTask {

    /* Time in which task is swapped to Cloud*/
    private Double swapTime;

    /* Task swapped*/
    private Boolean swapped;

    public TaskClassTwo() {
        this.swapTime = 0.0;
        this.swapped = false;
    }

    public TaskClassTwo(Double arrivalTime, Double serviceTime, Double swapTime, Boolean swapped) {
        super(arrivalTime, serviceTime);
        this.swapTime = swapTime;
        this.swapped = swapped;
    }

    /**
     * Compute Completion Time
     */
    public Double getCompletionTime() {
        return this.isSwapped()? this.getSwapTime() + this.getServiceTime() + AppConfiguration.SETUP_TIME : this.getArrivalTime() + this.getServiceTime();
    }

    /**
     * Getter e Setter
     */
    public Double getSwapTime() {
        return swapTime;
    }

    public void setSwapTime(Double swapTime) {
        this.swapTime = swapTime;
    }

    public Boolean isSwapped() {
        return swapped;
    }

    public void setSwapped(Boolean swapped) {
        this.swapped = swapped;
    }
}
