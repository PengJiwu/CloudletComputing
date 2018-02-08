package event;

import task.AbstractTask;

import java.util.Objects;

public abstract class AbstractEvent implements Comparable<AbstractEvent>{

    private double eventTime;
    private AbstractTask task;

    public AbstractEvent(double eventTime,AbstractTask task){
        this.eventTime = eventTime;
        this.task = task;
    }

    @Override
    public int compareTo(AbstractEvent o) {
        // necessary to ensure the proper functioning of the priority queue data structure
        if (this.eventTime < o.eventTime)
            return -1;
        else if (this.eventTime == o.eventTime)
            return 0;
        else
            return 1;
    }

    /**
     * Getter and setter
     */
    public double getEventTime() {
        return eventTime;
    }

    public AbstractTask getTask() {
        return task;
    }

    public void setTask(AbstractTask task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEvent that = (AbstractEvent) o;
        return Double.compare(that.eventTime, eventTime) == 0 ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTime, task);
    }
}
