package cloudlet;

import cloud.Cloud;
import config.AppConfiguration;
import task.AbstractTask;
import task.TaskClassOne;

public class Controller {

    /* Reference to cloud system*/
    private Cloud cloudService;

    /* Reference to cloudlet system*/
    private Cloudlet cloudletService;

    public Controller() {
        this.cloudletService = new Cloudlet();
        this.cloudService = new Cloud();
    }

    /**
     * Dispatching algorithm from text
     */
    public void handleArrival(AbstractTask task){
        //class 1 arrival
        if (task instanceof TaskClassOne){
            if (cloudletService.getN1() == AppConfiguration.N) {
                // cloudlet full
                // send the task to cloud
                cloudService.assignServer(task);
            }
            else if (cloudletService.getN1() + cloudletService.getN2() < AppConfiguration.S){
                // assign the task to cloudlet
                cloudletService.assignServer(task);
            }
            else if (cloudletService.getN2() > 0){
                // do preemption on class 2 task
                AbstractTask toStop = cloudletService.stopTask(task.getArrivalTime());
                // send the preempted task to cloud
                cloudService.assignServer(toStop);
                // accept the new task on cloudlet
                cloudletService.assignServer(task);
            }
            else {
                // accept the task on cloudlet
                cloudletService.assignServer(task);
            }
        }
        //class 2 arrival
        else{
            if (cloudletService.getN1()+cloudletService.getN2() >= AppConfiguration.S) {
                // threshold reached
                // send class 2 task to cloud
                cloudService.assignServer(task);
            }
            else {
                // threshold NOT reached
                // assign the task to cloudlet
                cloudletService.assignServer(task);
            }
        }
    }


    /**
     * Getter and Setter
     */
    public Cloud getCloudService() {
        return cloudService;
    }

    public Cloudlet getCloudletService() {
        return cloudletService;
    }
}
