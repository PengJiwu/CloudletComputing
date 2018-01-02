package task;

public class TaskClassOne extends AbstractTask {

    /**
     * Compute Completion Time
     */
    public Double getCompletionTime() {
        return this.getArrivalTime() + this.getServiceTime();
    }
}
