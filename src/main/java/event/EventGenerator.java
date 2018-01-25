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

        AbstractTask task;
        double arrival = Clock.getInstance().getArrival();
        arrival += distributions.exponential(lambaTot,2);
        double p1 = AppConfiguration.ARRIVAL_RATE_1 / lambaTot;
        distributions.selectStream(1);
        double typeProb = distributions.uniform(0.0,1.0);
        if(typeProb <= p1){
            task = new TaskClassOne(arrival);
        }
        else{
            task = new TaskClassTwo(arrival);
        }
        Clock.getInstance().setArrival(arrival);
        return new ArrivalEvent(task);
    }


}
