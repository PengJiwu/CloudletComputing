package cloudlet;

import cloud.Cloud;
import config.AppConfiguration;
import task.AbstractTask;
import task.TaskClassOne;
import task.TaskClassTwo;
import utils.Distributions;

public class Controller {

    /* Reference to cloud system*/
    private Cloud cloudService;

    /* Reference to cloudlet system*/
    private Cloudlet cloudletService;

    private Distributions distributions;

    public Controller() {
        this.cloudletService = new Cloudlet();
        this.cloudService = new Cloud();
        this.distributions = Distributions.getInstance();
    }

    /**
     * Assign right service Time to task and assign to Cloud Server
     */
    public void sendToCloud(AbstractTask task){
        if (task instanceof TaskClassOne){
            distributions.selectStream(2);//TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M1));
        }
        else{
            distributions.selectStream(3); // TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUD_M2));
            TaskClassTwo swapped = (TaskClassTwo) task;
            if (swapped.isSwapped())
                swapped.setSetupTime(distributions.exponential(AppConfiguration.SETUP_TIME));
            task = swapped;
        }
        cloudService.assignServer(task);
    }

    /**
     * Assign right service Time to task and assign to Cloudlet Server
     */
    public void acceptToCloudlet(AbstractTask task){
        if (task instanceof TaskClassOne){
            distributions.selectStream(2);//TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M1));
        }
        else{
            distributions.selectStream(3); // TODO usage of selectStream
            task.setServiceTime(distributions.exponential(AppConfiguration.CLOUDLET_M2));
        }
        cloudletService.assignServer(task);
    }

    /**
     * Dispatching algorithm from text
     */
    public void handleArrival(AbstractTask task){
        //class 1 arrival
        if (task instanceof TaskClassOne){
            if (cloudletService.getN1() == AppConfiguration.N)
                sendToCloud(task);
            else if (cloudletService.getN1()+cloudletService.getN2() < AppConfiguration.S){
                acceptToCloudlet(task);
                //TODO assign task to server
            }
            else if (cloudletService.getN2() > 0){
                //TODO preemption
                AbstractTask toStop = cloudletService.getStoppableTask();
                sendToCloud(toStop);
                acceptToCloudlet(task);
            }
            else
                acceptToCloudlet(task);
        }
        //class 2 arrival
        else{
            if (cloudletService.getN1()+cloudletService.getN2() >= AppConfiguration.S)
                sendToCloud(task);
            else
                acceptToCloudlet(task);
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
