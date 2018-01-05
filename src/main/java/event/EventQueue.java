package event;

import java.util.PriorityQueue;

public class EventQueue {

    private PriorityQueue<AbstractEvent> queue;

    public EventQueue(){
        this.queue = new PriorityQueue<>();
    }


    public AbstractEvent getFirstAvailableEvent(){
        return queue.poll();
    }

    public void addEvent(AbstractEvent event){
        this.queue.add(event);
    }

/*    public static void main(String[] args) {
        EventQueue q = new EventQueue();
        ArrivalEvent arrival1 = new ArrivalEvent(new TaskClassOne(0.5,1.5));
        ArrivalEvent arrival2 = new ArrivalEvent(new TaskClassOne(0.25,2));
        System.out.println(arrival1.toString());
        System.out.println(arrival2.toString());
        q.queue.add(arrival1);
        q.queue.add(arrival2);
        System.out.println(q.getFirstAvailableEvent().toString());
        System.out.println(q.getFirstAvailableEvent().toString());
    }*/
}
