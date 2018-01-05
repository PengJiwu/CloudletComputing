package event;

import task.AbstractTask;

public class CompletionEvent extends AbstractEvent{
    public CompletionEvent(AbstractTask task) {
        super(task.getCompletionTime(), task);
    }
}
