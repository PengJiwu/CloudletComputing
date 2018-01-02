package cloudlet;

import task.AbstractTask;

public class Cloudlet {

    /* Number of Class 1 Tasks*/
    private Integer n1;

    /* Number of Class 2 Tasks*/
    private Integer n2;

    //TODO completion as integer or Task?
    /* Number of Class 1 Tasks completed*/
    private Integer classOneCompletion;

    /* Number of Class 2 Tasks completed*/
    private Integer classTwoCompletion;

    //TODO list of servers as Integers or Server type
    // List<Server> servers;

    public Cloudlet() {
        this.n1 = 0;
        this.n2 = 0;
        this.classOneCompletion = 0;
        this.classTwoCompletion = 0;
    }

    //TODO implement
    public void assignServer(AbstractTask task){
    }

    //TODO implement and change return type if server list implemented as list of Server type
    public Integer getAvailableServer(){
        return 0;
    }

    //TODO implement
    public AbstractTask getStoppableTask() {
        return null;
    }

    //TODO implement
    public void removeTask (AbstractTask task){
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
