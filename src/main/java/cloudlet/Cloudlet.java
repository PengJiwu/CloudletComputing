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

    /* Task Class One last completion */
    private double lastCompletionClassOne;

    /* Task Class Two last completion */
    private double lastCompletionClassTwo;

    /* Percentage of Task Class Two */
    private double percentage2Preemption;

    /* Total number of Task Class Two preempted*/
    private int totalClassTwoPreemption;

    /* Total number of Task Class Two assigned to Cloudlet*/
    private int totalClassTwoAssigned;

    private double lastPreemptionTime;


    public Cloudlet() {
        this.n1 = 0;
        this.n2 = 0;
        this.classOneCompletion = 0;
        this.classTwoCompletion = 0;
        this.distributions = Distributions.getInstance();
        this.taskList = new ArrayList<>();
        this.eventQueue = EventQueue.getInstance();
        this.lastCompletionClassOne = 0.0;
        this.lastCompletionClassTwo = 0.0;
        this.percentage2Preemption = 0.0;
        this.lastPreemptionTime = 0.0;
    }

    public void assignServer(AbstractTask task){
        if (task instanceof TaskClassOne){
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M1,4));
        }
        else{
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M2,5));
        }
        this.incrementPopulation(task);
        taskList.add(task);
        AbstractEvent toPush = new CompletionEvent(task);
        eventQueue.addEvent(toPush);

        //System.out.println("TaskListCloudlet size = " + taskList.size());

    }

    public AbstractTask stopTask(double swappedTime) {
        TaskClassTwo toStop = null;
        toStop = (TaskClassTwo) findTaskWithMaxCompletionTime();

        if (toStop!=null) {
            taskList.remove(toStop);

            eventQueue.dropElement(new CompletionEvent(toStop));

            toStop.setSwapped(true);
            this.n2--;

            this.totalClassTwoPreemption++;
            updatePercentage2Preemption();

            toStop.setSwapTime(swappedTime);

            // take track of the preemption instant time
            this.lastPreemptionTime = swappedTime;
        }

        return toStop;
    }

    protected AbstractTask findTaskWithMaxCompletionTime() {
        double maxTime = 0.0;
        AbstractTask toStop = null;

        for (AbstractTask task : taskList) {
            if (task instanceof TaskClassTwo){
                if (maxTime<= task.getCompletionTime()) {
                    maxTime = task.getCompletionTime();
                    toStop = task;
                }
            }
        }
        return toStop;
    }

    private void updatePercentage2Preemption() {
        this.percentage2Preemption = (double) this.totalClassTwoPreemption / (double) this.totalClassTwoAssigned;
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

    private void incrementPopulation(AbstractTask task){
        if (task instanceof TaskClassOne) {
            this.n1++;
        }
        else {
            this.n2++;
            this.totalClassTwoAssigned++;
        }
    }

    public void handleCompletion(AbstractTask task) {

        //System.out.println("C CloudletVariables: N1 = " + this.getN1() + " N2 = " + this.getN2());

        taskList.remove(task);
        if (task instanceof TaskClassOne){
            this.n1--;
            this.classOneCompletion++;
            this.lastCompletionClassOne = task.getCompletionTime();
        }
        else{
            this.n2--;
            this.classTwoCompletion++;
            this.lastCompletionClassTwo = task.getCompletionTime();
        }

        //System.out.println("C CloudletVariables post completion: N1 = " + this.getN1() + " N2 = " + this.getN2());
        //System.out.println("Task removed: " + task.toString());

        //System.out.println("TaskListCloud size = " + taskList.size());
        //System.out.println("TaskList: " + taskList.toString());
        //System.out.println("");
    }

    public double getLastCompletion(){
        return Double.max(this.lastCompletionClassOne,this.lastCompletionClassTwo);
    }


    /**
     * Getter and Setter
     */
    public Integer getN1() {
        return n1;
    }

    public void setN1(Integer n1) {
        this.n1 = n1;
    }

    public Integer getN2() {
        return n2;
    }

    public void setN2(Integer n2) {
        this.n2 = n2;
    }

    public Integer getClassOneCompletion() {
        return classOneCompletion;
    }

    public void setClassOneCompletion(Integer classOneCompletion) {
        this.classOneCompletion = classOneCompletion;
    }

    public Integer getClassTwoCompletion() {
        return classTwoCompletion;
    }

    public void setClassTwoCompletion(Integer classTwoCompletion) {
        this.classTwoCompletion = classTwoCompletion;
    }

    public double getLastCompletionClassOne() {
        return lastCompletionClassOne;
    }

    public void setLastCompletionClassOne(double lastCompletionClassOne) {
        this.lastCompletionClassOne = lastCompletionClassOne;
    }

    public double getLastCompletionClassTwo() {
        return lastCompletionClassTwo;
    }

    public void setLastCompletionClassTwo(double lastCompletionClassTwo) {
        this.lastCompletionClassTwo = lastCompletionClassTwo;
    }

    public double getLastPreemptionTime() {
        return lastPreemptionTime;
    }
}
