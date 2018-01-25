package task;

public class TaskClassTwo extends AbstractTask {

    /* Time in which task is swapped to Cloud*/
    private double swapTime;

    /* Task swapped*/
    private Boolean swapped;

    /* Task swapped*/
    private double setupTime;

    public TaskClassTwo(double arrivalTime, double serviceTime, double swapTime, double setupTime, Boolean swapped) {
        super(arrivalTime, serviceTime);
        this.swapTime = swapTime;
        this.swapped = swapped;
        this.setupTime = setupTime;
    }

    public TaskClassTwo(double arrivalTime){
        this(arrivalTime,0.0,0.0,0.0,false);
    }

    /**
     * Compute Completion Time
     */
    public double getCompletionTime() {
        return this.isSwapped()
                ? this.getSwapTime() + this.getServiceTime() + this.getSetupTime()
                : this.getArrivalTime() + this.getServiceTime();
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

    public double getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(double setupTime) {
        this.setupTime = setupTime;
    }

}
