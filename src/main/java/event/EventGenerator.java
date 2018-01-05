package event;

import config.AppConfiguration;
import library.Rvgs;
import library.Rngs;
import task.AbstractTask;
import task.TaskClassOne;
import task.TaskClassTwo;
import utils.Clock;

public class EventGenerator {

    private Rvgs rvgs;
    private Rngs rngs;

    public EventGenerator(){
        this.rngs = new Rngs();
        this.rngs.plantSeeds(123456789);
        this.rvgs = new Rvgs(this.rngs);
    }

    //TODO missing queue push
    public ArrivalEvent generateArrival(){
        rvgs.rngs.selectStream(1);
        double type = rvgs.uniform(1.0, 2.0);
        double arrival = Clock.getInstance().getArrival();
        AbstractTask task;
        if(type <= 1.5){
            rvgs.rngs.selectStream(2);
            arrival += rvgs.exponential(1/ AppConfiguration.ARRIVAL_RATE_1);
            task = new TaskClassOne(arrival);
        }
        else{
            rvgs.rngs.selectStream(3);
            arrival += rvgs.exponential(1/AppConfiguration.ARRIVAL_RATE_2);
            task = new TaskClassTwo(arrival);
        }
        Clock.getInstance().setArrival(arrival);
        return new ArrivalEvent(task);
    }

/*    public static void main(String[] args) {
        AppConfiguration.readConfiguration();
        EventGenerator eg = new EventGenerator();
        ArrivalEvent test = eg.generateArrival();
        ArrivalEvent test1 = eg.generateArrival();
        ArrivalEvent test2 = eg.generateArrival();
        ArrivalEvent test3 = eg.generateArrival();
        ArrivalEvent test4 = eg.generateArrival();
        ArrivalEvent test5 = eg.generateArrival();
        ArrivalEvent test6 = eg.generateArrival();
        ArrivalEvent test7 = eg.generateArrival();
        System.out.println(test.toString());
        System.out.println(test1.toString());
        System.out.println(test2.toString());
        System.out.println(test3.toString());
        System.out.println(test4.toString());
        System.out.println(test5.toString());
        System.out.println(test6.toString());
        System.out.println(test7.toString());
    }*/
}
