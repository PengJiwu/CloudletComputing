package event;

import config.AppConfiguration;
import task.AbstractTask;
import task.TaskClassOne;
import task.TaskClassTwo;
import utils.Clock;
import utils.Distributions;

public class EventGenerator {

    private Distributions distributions;

    public EventGenerator(){
        distributions = Distributions.getInstance();
    }


    public ArrivalEvent generateArrival(){

        distributions.selectStream(1);
        double type = distributions.uniform(1.0,2.0);
        double arrival = Clock.getInstance().getArrival();
        AbstractTask task;
        if(type <= 1.5){
            arrival += distributions.exponential(AppConfiguration.ARRIVAL_RATE_1,2);
            task = new TaskClassOne(arrival);
        }
        else{
            arrival += distributions.exponential(AppConfiguration.ARRIVAL_RATE_2,3);
            task = new TaskClassTwo(arrival);
        }

        Clock.getInstance().setArrival(arrival);
        //System.out.println("Task arrival: " + task.toString());
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
