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


    public Cloudlet() {
        this.n1 = 0;
        this.n2 = 0;
        this.classOneCompletion = 0;
        this.classTwoCompletion = 0;
        this.distributions = Distributions.getInstance();
        this.taskList = new ArrayList<>();
        this.eventQueue = EventQueue.getInstance();
    }

    public void assignServer(AbstractTask task){
        if (task instanceof TaskClassOne){
            distributions.selectStream(2);//TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M1));
        }
        else{
            distributions.selectStream(3); // TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M2));
        }
        this.incrementPopulation(task);
        taskList.add(task);
        AbstractEvent toPush = new CompletionEvent(task);
        eventQueue.addEvent(toPush);

    }

    public AbstractTask stopTask(double swappedTime) {
        TaskClassTwo toStop = null;
        double max = 0.0;
        int i = 0,index=0;
        for (AbstractTask task : taskList){
            if (task instanceof TaskClassTwo){
                if (toStop == null)
                    toStop = (TaskClassTwo) task;
                else if (task.getCompletionTime() > toStop.getCompletionTime())
                    toStop = (TaskClassTwo) task;
                index = i;
            }
            i++;
        }
        taskList.remove(index);
        distributions.selectStream(2);//TODO usage of selectStream
        eventQueue.dropElement(new CompletionEvent(toStop));
        toStop.setSwapped(true);
        this.n2--;
        toStop.setSwapTime(swappedTime);
        toStop.setSetupTime(distributions.exponential(AppConfiguration.SETUP_TIME));
        return toStop;
    }


    private void incrementPopulation(AbstractTask task){
        if (task instanceof TaskClassOne)
            this.n1++;
        else
            this.n2++;
    }

    public void handleCompletion(AbstractTask task) {
        taskList.remove(task);
        if (task instanceof TaskClassOne){
            this.n1--;
            this.classOneCompletion++;
        }
        else{
            this.n2--;
            this.classTwoCompletion++;
        }
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
}
