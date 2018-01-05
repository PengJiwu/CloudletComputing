package event;

import task.AbstractTask;

public abstract class AbstractEvent implements Comparable<AbstractEvent>{

    private double eventTime;
    private AbstractTask task;

    public AbstractEvent(double eventTime,AbstractTask task){
        this.eventTime = eventTime;
        this.task = task;
    }


    @Override
    public int compareTo(AbstractEvent o) {
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

    public void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    public AbstractTask getTask() {
        return task;
    }

    public void setTask(AbstractTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "AbstractEvent{" +
                "eventTime=" + eventTime +
                ", task=" + task.toString() +
                '}';
    }
}
