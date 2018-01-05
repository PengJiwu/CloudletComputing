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

    public void sendToCloud(AbstractTask task){
        cloudService.assignServer(task);
    }

    public void handleArrival(AbstractTask task){
        //class 1 arrival
        if (task instanceof TaskClassOne){
            if (cloudletService.getN1() == AppConfiguration.N)
                sendToCloud(task);
            else if (cloudletService.getN1()+cloudletService.getN2() < AppConfiguration.S){
                cloudletService.assignServer(task);
                //TODO assign task to server
            }
            else if (cloudletService.getN2() > 0){
                //TODO preemption
                AbstractTask toStop = cloudletService.getStoppableTask();
                sendToCloud(toStop);
            }
            else
                cloudletService.assignServer(task);
        }
        //class 2 arrival
        else{
            if (cloudletService.getN1()+cloudletService.getN2() >= AppConfiguration.S)
                sendToCloud(task);
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

    public void setCloudService(Cloud cloudService) {
        this.cloudService = cloudService;
    }

    public Cloudlet getCloudletService() {
        return cloudletService;
    }

    public void setCloudletService(Cloudlet cloudletService) {
        this.cloudletService = cloudletService;
    }
}
