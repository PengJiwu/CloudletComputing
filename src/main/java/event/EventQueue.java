package event;

import java.util.PriorityQueue;

public class EventQueue {

    private static EventQueue instance = null;

    private PriorityQueue<AbstractEvent> queue;

    private EventQueue(){
        this.queue = new PriorityQueue<>();
    }

    public static EventQueue getInstance(){
        if (instance == null)
            instance = new EventQueue();
        return instance;
    }

    public AbstractEvent getFirstAvailableEvent(){
        return queue.poll();
    }

    public void addEvent(AbstractEvent event){
        this.queue.add(event);
    }

    public void dropElement(AbstractEvent toDrop) {
        this.queue.remove(toDrop);
    }

    public int getQueueSize() {
        return this.queue.size();
    }

    public static void fill() {
        instance = null;
    }
}
