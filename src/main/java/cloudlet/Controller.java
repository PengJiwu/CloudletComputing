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
                cloudService.assignServer(task);
            }
            else if (cloudletService.getN1() + cloudletService.getN2() < AppConfiguration.S){
                cloudletService.assignServer(task);
            }
            else if (cloudletService.getN2() > 0){
                AbstractTask toStop = cloudletService.stopTask(task.getArrivalTime());
                cloudService.assignServer(toStop);
                cloudletService.assignServer(task);
            }
            else
                cloudletService.assignServer(task);
        }
        //class 2 arrival
        else{
            if (cloudletService.getN1()+cloudletService.getN2() >= AppConfiguration.S)
                cloudService.assignServer(task);
            else
                cloudletService.assignServer(task);
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
