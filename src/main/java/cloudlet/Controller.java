package cloudlet;

import cloud.Cloud;
import task.AbstractTask;

public class Controller {

    /* Reference to cloud system*/
    private Cloud cloudService;

    /* Reference to cloudletsystem*/
    private Cloudlet cloudletService;

    public Controller() {
        this.cloudletService = new Cloudlet();
        this.cloudService = new Cloud();
    }

    //TODO implement
    public void sendToCloud(AbstractTask task){}

    //TODO implement
    public void handleTask(AbstractTask task){}

    //TODO implement
    public void handleEventQueue(){}


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
