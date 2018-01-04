package task;

public class TaskClassOne extends AbstractTask {

    /**
     * Compute Completion Time
     */
    public double getCompletionTime() {
        return this.getArrivalTime() + this.getServiceTime();
    }
}
