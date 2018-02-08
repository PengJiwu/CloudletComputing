package cloudlet;

import config.AppConfiguration;
import event.AbstractEvent;
import event.CompletionEvent;
import event.EventQueue;
import task.AbstractTask;
import task.TaskClassOne;
import task.TaskClassTwo;
import utils.Distributions;

import java.util.ArrayList;
import java.util.List;

public class Cloudlet {

    /* Number of Class 1 Tasks*/
    private Integer n1;

    /* Number of Class 2 Tasks*/
    private Integer n2;

    /* Number of Class 1 Tasks completed*/
    private Integer classOneCompletion;

    /* Number of Class 2 Tasks completed*/
    private Integer classTwoCompletion;

    /* Distributions Library*/
    private Distributions distributions;

    /* List of Task assigned */
    private List<AbstractTask> taskList;

    /* Event queue */
    private EventQueue eventQueue;

    /* Percentage of Task Class Two */
    private double percentage2Preemption;

    /* Total number of Task Class Two preempted*/
    private int totalClassTwoPreemption;

    /* Total number of Task Class Two assigned to Cloudlet*/
    private int totalClassTwoAssigned;



    public Cloudlet() {
        this.n1 = 0;
        this.n2 = 0;
        this.classOneCompletion = 0;
        this.classTwoCompletion = 0;
        this.distributions = Distributions.getInstance();
        this.taskList = new ArrayList<>();
        this.eventQueue = EventQueue.getInstance();
        this.percentage2Preemption = 0.0;

    }

    public void assignServer(AbstractTask task){
        if (task instanceof TaskClassOne){
            // set the service time for class one task
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M1,4));
        }
        else{
            // set the service time for class two task
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M2,5));
        }
        // update state variables
        this.incrementPopulation(task);
        taskList.add(task);
        // create completion event
        AbstractEvent toPush = new CompletionEvent(task);
        // add completion event to event queue
        eventQueue.addEvent(toPush);
    }

    public AbstractTask stopTask(double swappedTime) {
        TaskClassTwo toStop = null;
        // find task with max completion time in the future
        toStop = (TaskClassTwo) findTaskWithMaxCompletionTime();

        if (toStop!=null) {
            // remove task from cloudlet
            taskList.remove(toStop);
            // remove old completion event
            eventQueue.dropElement(new CompletionEvent(toStop));

            // set preemption flag to task
            toStop.setSwapped(true);
            toStop.setSwapTime(swappedTime);

            // update state variable
            this.n2--;
            this.totalClassTwoPreemption++;
            updatePercentage2Preemption();
        }
        // return the preempted task
        return toStop;
    }

    protected AbstractTask findTaskWithMaxCompletionTime() {
        double maxTime = 0.0;
        AbstractTask toStop = null;

        // scroll through the list
        for (AbstractTask task : taskList) {
            if (task instanceof TaskClassTwo){
                if (maxTime<= task.getCompletionTime()) {
                    maxTime = task.getCompletionTime();
                    // find the task with max completion time
                    toStop = task;
                }
            }
        }
        return toStop;
    }

    public void handleCompletion(AbstractTask task) {
        // remove task from list
        taskList.remove(task);
        // update state variables
        if (task instanceof TaskClassOne){
            this.n1--;
            this.classOneCompletion++;
        }
        else{
            this.n2--;
            this.classTwoCompletion++;
        }
    }

    private void incrementPopulation(AbstractTask task){
        if (task instanceof TaskClassOne) {
            this.n1++;
        }
        else {
            this.n2++;
            this.totalClassTwoAssigned++;
        }
    }


    private void updatePercentage2Preemption() {
        // update percentage of preempted jobs
        this.percentage2Preemption = (double) this.totalClassTwoPreemption / (double) this.totalClassTwoAssigned;
    }


    /**
     * Getter and Setter
     */
    public Integer getN1() {
        return n1;
    }

    public double getPercentage2Preemption() {
        return percentage2Preemption;
    }

    public Integer getTotalClassTwoPreemption() {
        return totalClassTwoPreemption;
    }

    public Integer getTotalClassTwoAssigned() {
        return totalClassTwoAssigned;
    }

    public Integer getN2() {
        return n2;
    }

    public Integer getClassOneCompletion() {
        return classOneCompletion;
    }

    public Integer getClassTwoCompletion() {
        return classTwoCompletion;
    }

}
