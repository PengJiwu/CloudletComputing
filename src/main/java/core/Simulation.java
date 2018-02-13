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
        if (AppConfiguration.TEST_S){
            // run the tests by varying the value of S
            multipleTest();
        }
        else{
            // run test on single value of S
            singleTest();
        }
        System.exit(0);
    }

    public static void multipleTest(){
        for (;AppConfiguration.S > 0 ; AppConfiguration.S --){
            System.out.println("Starting simulation for S = " + AppConfiguration.S);
            // prepare data structures
            setupEnvironment();
            run();
            // clear data structures
            cleanEnvironment();
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    public static void singleTest() {
        // prepare data structures
        setupEnvironment();
        run();
        // clear data structures
        cleanEnvironment();
    }

    private static void setupEnvironment() {
        controller = new Controller();
        clock = Clock.getInstance();
        eventGenerator = new EventGenerator();
        eventQueue = EventQueue.getInstance();
        performance = new Performance(controller);
    }

    private static void cleanEnvironment(){
        performance.closeWriters();
        // reset singleton istance
        Clock.restart();
        // empty event queue
        EventQueue.fill();

    }

    private static void run(){
        int i = 1;
        int stopJobs = AppConfiguration.BATCH_SIZE * AppConfiguration.NUM_BATCH;

        // start simulation and do completion while the event queue has at least an event (completion or arrival)
        while (clock.getArrival() == AppConfiguration.START || eventQueue.getQueueSize() > 0){
            // if current clock instant is before the close door time
            // stop generating arrival event
            if (clock.getArrival() < AppConfiguration.STOP
                    && i < stopJobs
                    ){
                AbstractEvent event = eventGenerator.generateArrival();
                eventQueue.addEvent(event);
                i++;
            }
            // get first event in order of event time
            AbstractEvent toHandle = eventQueue.getFirstAvailableEvent();
            // update the next event of clock
            clock.setNext(toHandle.getEventTime());
            // update statistics area
            performance.updateArea();
            // set the current event of clock
            clock.setCurrent(clock.getNext());
            // verify type of event
            if (toHandle instanceof ArrivalEvent){
                // arrival event type
                controller.handleArrival(toHandle.getTask());
            }
            else{
                // completion event type
                if (toHandle.getTask().isCloudlet()){
                    // completion event on cloudlet
                    performance.handleCloudletCompletion(toHandle.getTask());
                    controller.getCloudletService().handleCompletion(toHandle.getTask());
                }
                else{
                    // completion event on cloud
                    performance.handleCloudCompletion(toHandle.getTask());
                    controller.getCloudService().handleCompletion(toHandle.getTask());
                }
            }
        }
        // print result on screen
        performance.printResults();


        double p = controller.getCloudletService().getClassOneCompletion()
                / (double) (controller.getCloudletService().getClassOneCompletion() + controller.getCloudService().getClassOneCompletion());

        double q = (controller.getCloudletService().getClassTwoCompletion() + controller.getCloudletService().getTotalClassTwoPreemption())
                / (double) ( controller.getCloudService().getClassTwoCompletion()
                + controller.getCloudletService().getClassTwoCompletion());

        double r = (controller.getCloudletService().getTotalClassTwoPreemption()) / (double)
                (controller.getCloudService().getClassTwoCompletion() +
                controller.getCloudletService().getClassTwoCompletion()+
                controller.getCloudletService().getClassOneCompletion()+
               controller.getCloudService().getClassOneCompletion());

        System.out.println("p = "+p);
        System.out.println("q = "+q);
        System.out.println("r = "+r);


    }
}
