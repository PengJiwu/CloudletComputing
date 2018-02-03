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
            multipleTest();
        }
        else{
            singleTest();
        }
        System.exit(0);
    }

    public static void multipleTest(){
        for (;AppConfiguration.S > 0 ; AppConfiguration.S --){
            System.out.println("Starting simulation for S = " + AppConfiguration.S);
            setupEnvironment();
            run();
            cleanEnvironment();
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    public static void singleTest() {
        setupEnvironment();
        run();
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
        Clock.restart();
        EventQueue.fill();

    }

    private static void run(){
        int i = 1;
        int stopJobs = AppConfiguration.BATCH_SIZE * AppConfiguration.NUM_BATCH;

        while (clock.getArrival() == AppConfiguration.START || eventQueue.getQueueSize() > 0){
            if (clock.getArrival() < AppConfiguration.STOP
                    && i < stopJobs
                    ){
                AbstractEvent event = eventGenerator.generateArrival();
                eventQueue.addEvent(event);
                i++;
            }
            AbstractEvent toHandle = eventQueue.getFirstAvailableEvent();
            clock.setNext(toHandle.getEventTime());
            performance.updateArea();
            clock.setCurrent(clock.getNext());
            if (toHandle instanceof ArrivalEvent){
                controller.handleArrival(toHandle.getTask());
            }
            else{
                if (toHandle.getTask().isCloudlet()){
                    performance.handleCloudletCompletion(toHandle.getTask());
                    controller.getCloudletService().handleCompletion(toHandle.getTask());
                }
                else{
                    performance.handleCloudCompletion(toHandle.getTask());
                    controller.getCloudService().handleCompletion(toHandle.getTask());
                }
            }
        }
        performance.printResults();

        /*
        double p = controller.getCloudletService().getClassOneCompletion()
                / (double) (controller.getCloudletService().getClassOneCompletion() + controller.getCloudService().getClassOneCompletion());

        double q = (controller.getCloudletService().getClassTwoCompletion() + controller.getCloudletService().getTotalClassTwoPreemption())
                / (double) ( controller.getCloudService().getClassTwoCompletion()
                + controller.getCloudletService().getClassTwoCompletion());

        double r = (controller.getCloudletService().getTotalClassTwoPreemption()) / (double)
                (controller.getCloudletService().getClassOneCompletion() +
                controller.getCloudletService().getClassTwoCompletion() +
                controller.getCloudletService().getTotalClassTwoPreemption());

        System.out.println("p = "+p);
        System.out.println("q = "+q);
        System.out.println("r = "+r);
        */

    }
}
