package core;

import cloudlet.Controller;
import config.AppConfiguration;
import event.AbstractEvent;
import event.ArrivalEvent;
import event.EventGenerator;
import event.EventQueue;
import stat.Performance;
import utils.Clock;

public class Simulation {

    public static Controller controller;
    public static Clock clock;
    public static EventGenerator eventGenerator;
    public static EventQueue eventQueue;
    public static Performance performance;

    public static void main(String[] args) {
        AppConfiguration.readConfiguration();
        setupEnvironment();
        run();
        //printSystemValues();
        System.exit(0);
    }

    private static void setupEnvironment() {
        controller = new Controller();
        clock = Clock.getInstance();
        eventGenerator = new EventGenerator();
        eventQueue = EventQueue.getInstance();
        performance = new Performance(controller);
    }

    private static void run(){
        //int i = 0;


        while (clock.getArrival() == AppConfiguration.START || eventQueue.getQueueSize() > 0){
            if (clock.getArrival() < AppConfiguration.STOP /*&& i<10*/){
                AbstractEvent event = eventGenerator.generateArrival();
                eventQueue.addEvent(event);

            }
            //System.out.println(eventQueue.toString());

            AbstractEvent toHandle = eventQueue.getFirstAvailableEvent();
            //System.out.println("Task to handle: " + toHandle.toString());



            clock.setNext(toHandle.getEventTime());

            performance.updateArea();

            clock.setCurrent(clock.getNext());



            if (toHandle instanceof ArrivalEvent){
                controller.handleArrival(toHandle.getTask());
            }
            else{

                if (toHandle.getTask().isCloudlet())
                    controller.getCloudletService().handleCompletion(toHandle.getTask());
                    //aggiornare responseTime
                else
                    controller.getCloudService().handleCompletion(toHandle.getTask());
                    //aggiornare responseTime
            }
            //i++;


        }
    }
}
