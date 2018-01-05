package core;

import cloudlet.Controller;
import config.AppConfiguration;
import event.AbstractEvent;
import event.ArrivalEvent;
import event.EventGenerator;
import event.EventQueue;
import utils.Clock;

public class Simulation {

    public static Controller controller;
    public static Clock clock;
    public static EventGenerator eventGenerator;
    public static EventQueue eventQueue;

    public static void main(String[] args) {
        AppConfiguration.readConfiguration();
        setupEnvironment();
        run();
        System.exit(0);
    }

    private static void setupEnvironment() {
        controller = new Controller();
        clock = Clock.getInstance();
        eventGenerator = new EventGenerator();
        eventQueue = new EventQueue();
    }

    private static void run(){
        while (clock.getArrival() < AppConfiguration.STOP){
            AbstractEvent event = eventGenerator.generateArrival();
            eventQueue.addEvent(event);
            AbstractEvent toHandle = eventQueue.getFirstAvailableEvent();
            clock.setCurrent(toHandle.getEventTime());
            if (toHandle instanceof ArrivalEvent){
                controller.handleArrival(toHandle.getTask());
            }
            else{
                //someone.handleCompletion(toHandle.getTask());
            }
        }
    }
}
