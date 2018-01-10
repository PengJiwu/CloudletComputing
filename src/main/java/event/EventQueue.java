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

    @Override
    public String toString() {
        return "EventQueue{" +
                "queue=" + queue.toString() +
                '}';
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

/*    public static void main(String[] args) {
        EventQueue q = new EventQueue();
        ArrivalEvent arrival1 = new ArrivalEvent(new TaskClassOne(0.5,1.5));
        ArrivalEvent arrival2 = new ArrivalEvent(new TaskClassOne(0.25,2));
        q.queue.add(arrival1);
        q.queue.add(arrival2);
        ArrivalEvent arrival3 = new ArrivalEvent(new TaskClassOne(0.5));
        q.dropElement(arrival3);
        System.out.println(q.getFirstAvailableEvent().toString());
        System.out.println(q.getFirstAvailableEvent().toString());

    }*/
}
