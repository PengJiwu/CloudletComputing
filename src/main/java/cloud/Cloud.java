package cloud;

import task.AbstractTask;

public class Cloud {


    //TODO completion as integer or Task?
    /* Number of Class 1 Tasks completed*/
    private Integer classOneCompletion;

    /* Number of Class 2 Tasks completed*/
    private Integer classTwoCompletion;

    //TODO list of servers as Integers or Server type
    //List<Server> servers;

    public Cloud() {
        this.classOneCompletion = 0;
        this.classTwoCompletion = 0;
    }

    //TODO implement
    public void assignServer(AbstractTask task){
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
}
