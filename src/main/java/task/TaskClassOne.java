package task;

public class TaskClassOne extends AbstractTask {


    public TaskClassOne(double arrivalTime) {
        super(arrivalTime,0.0);
    }

    /**
     * Compute Completion Time
     */
    public double getCompletionTime() {
        return this.getArrivalTime() + this.getServiceTime();
    }

}
