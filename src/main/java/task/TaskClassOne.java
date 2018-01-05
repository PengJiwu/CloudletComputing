package task;

public class TaskClassOne extends AbstractTask {


    public TaskClassOne(double arrivalTime) {
        super(arrivalTime,0.0);
    }

    public TaskClassOne(double arrivalTime, double serviceTime){
        super(arrivalTime,serviceTime);
    }
    /**
     * Compute Completion Time
     */
    public double getCompletionTime() {
        return this.getArrivalTime() + this.getServiceTime();
    }

    @Override
    public String toString() {
        return "TaskClassOne{" +
                "arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
