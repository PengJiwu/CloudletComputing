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
            distributions.selectStream(2);//TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M1));
        }
        else {
            distributions.selectStream(3); // TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M2));
            TaskClassTwo swapped = (TaskClassTwo) task;
            if (swapped.isSwapped())
                swapped.setSetupTime(distributions.exponential(AppConfiguration.SETUP_TIME));
            task = swapped;
        }
        this.incrementPopulation(task);
        taskList.add(task);
        task.setCloudlet(false);
        AbstractEvent toPush = new CompletionEvent(task);
        eventQueue.addEvent(toPush);

        //System.out.println("TaskListCloud size = " + taskList.size());
    }

    private void incrementPopulation(AbstractTask task){
        if (task instanceof TaskClassOne)
            this.n1++;
        else
            this.n2++;
    }

    public void handleCompletion(AbstractTask task) {

        //System.out.println("C CloudVariables: N1 = " + this.getN1() + " N2 = " + this.getN2());

        taskList.remove(task);
        if (task instanceof TaskClassOne){
            this.n1--;
            this.classOneCompletion++;
        }
        else{
            this.n2--;
            this.classTwoCompletion++;
        }

       //System.out.println("C CloudletVariables post completion: N1 = " + this.getN1() + " N2 = " + this.getN2());
       //System.out.println("Task removed: " + task.toString());
       //System.out.println("TaskListCloud size = " + taskList.size());
       //System.out.println("");
    }


    /**
     * Getter and Setter
     */
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

    public List<AbstractTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<AbstractTask> taskList) {
        this.taskList = taskList;
    }


}
