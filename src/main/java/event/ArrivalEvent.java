package event;

import task.AbstractTask;

public class ArrivalEvent extends AbstractEvent{

    public ArrivalEvent(AbstractTask task) {
        super(task.getArrivalTime(), task);
    }
}
