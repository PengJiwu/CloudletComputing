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
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M1,6));
        }
        else {
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M2,7));
            TaskClassTwo swapped = (TaskClassTwo) task;
            if (swapped.isSwapped())
                swapped.setSetupTime(distributions.exponential(AppConfiguration.SETUP_TIME,8));
            task = swapped;
        }
        this.incrementPopulation(task);
        taskList.add(task);
        task.setCloudlet(false);
        AbstractEvent toPush = new CompletionEvent(task);
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
        taskList.remove(task);
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
