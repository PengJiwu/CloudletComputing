package cloud;

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

public class Cloud {

    /* Number of Class 1 Tasks*/
    private Integer n1;

    /* Number of Class 2 Tasks*/
    private Integer n2;

    /* Number of Class 1 Tasks completed*/
    private Integer classOneCompletion;

    /* Number of Class 2 Tasks completed*/
    private Integer classTwoCompletion;

    /* List of task in cloud*/
    private List<AbstractTask> taskList;

    /* Distributions Library*/
    private Distributions distributions;

    /* Event queue */
    private EventQueue eventQueue;


    public Cloud() {
        this.n1 = 0;
        this.n2 = 0;
        this.classOneCompletion = 0;
        this.classTwoCompletion = 0;
        this.taskList = new ArrayList<>();
        this.distributions = Distributions.getInstance();
        this.eventQueue = EventQueue.getInstance();
    }

    public void assignServer(AbstractTask task){
        if (task instanceof TaskClassOne){
            // set the service time for class one task
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M1,6));
        }
        else {
            // set the service time for class two task
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M2,7));
            TaskClassTwo swapped = (TaskClassTwo) task;
            // if the task was preempted
            if (swapped.isSwapped())
                // set the setup time to the task
                swapped.setSetupTime(distributions.exponential(1/AppConfiguration.SETUP_TIME,8));
            task = swapped;
        }
        // update state variables
        this.incrementPopulation(task);
        taskList.add(task);
        task.setCloudlet(false);
        // create completion event
        AbstractEvent toPush = new CompletionEvent(task);
        // add completion event to event queue
        eventQueue.addEvent(toPush);
    }

    private void incrementPopulation(AbstractTask task){
        if (task instanceof TaskClassOne) {
            this.n1++;
        }
        else {
            this.n2++;
        }
    }

    public void handleCompletion(AbstractTask task) {
        // remove the task
        taskList.remove(task);
        // update state variables
        if (task instanceof TaskClassOne){
            this.classOneCompletion++;
            this.n1--;
        }
        else{
            this.classTwoCompletion++;
            this.n2--;
        }
    }


    /**
     * Getter and Setter
     */
    public Integer getClassOneCompletion() {
        return classOneCompletion;
    }

    public Integer getClassTwoCompletion() {
        return classTwoCompletion;
    }

    public Integer getN1() {
        return n1;
    }

    public Integer getN2() {
        return n2;
    }

}
