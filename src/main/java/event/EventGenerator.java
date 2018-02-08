package event;

import config.AppConfiguration;
import task.AbstractTask;
import task.TaskClassOne;
import task.TaskClassTwo;
import utils.Clock;
import utils.Distributions;

public class EventGenerator {

    private Distributions distributions;
    private double lambaTot;

    public EventGenerator(){
        lambaTot = AppConfiguration.ARRIVAL_RATE_1 + AppConfiguration.ARRIVAL_RATE_2;
        distributions = Distributions.getInstance();
    }

    public ArrivalEvent generateArrival(){

        // generic task type
        AbstractTask task;
        // get the time of the last arrival event
        double arrival = Clock.getInstance().getArrival();
        // increment arrival time by the new value
        // necessary to guarantee the increasing monotony of time
        arrival += distributions.exponential(lambaTot,2);
        // probability that a task is class 1
        double p1 = AppConfiguration.ARRIVAL_RATE_1 / lambaTot;
        distributions.selectStream(1);
        // generate an uniform probability
        double typeProb = distributions.uniform(0.0,1.0);
        if(typeProb <= p1){
            // according to the probability generated
            // create a class 1 task
            task = new TaskClassOne(arrival);
        }
        else{
            // else create a class 2 task
            task = new TaskClassTwo(arrival);
        }
        // set the last arrival time in the clock
        Clock.getInstance().setArrival(arrival);
        return new ArrivalEvent(task);
    }


}
