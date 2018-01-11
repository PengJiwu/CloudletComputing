package stat;

import cloudlet.Controller;
import utils.Clock;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by maurizio on 11/01/18.
 */
public class Performance {


    public static Area systemArea;
    public static Area cloudletArea;
    public static Area cloudlet1Area;
    public static Area cloudlet2Area;
    public static Area cloudArea;
    public static Area cloud1Area;
    public static Area cloud2Area;

    Clock clock;
    Controller controller;
    public int n1, n2, cloud_n1, cloud_n2;
    public int number = 0;

    public double cloudletResponseTime;
    public double cloudlet1ResponseTime;
    public double cloudlet2ResponseTime;

    public Performance(Controller controller) {

        this.controller = controller;
        clock = Clock.getInstance();
        systemArea = new Area();
        cloudletArea = new Area();
        cloudlet1Area = new Area();
        cloudlet2Area = new Area();
        cloudArea = new Area();
        cloud1Area = new Area();
        cloud2Area = new Area();
        n1 = 0;
        n2 = 0;
        cloud_n1 = 0;
        cloud_n2 = 0;
    }

//

    /**
     * creates file for ResponseTime and Population for single class and global for cloudlet and cloud
     *
     */
    public static void createFile() {
        String path;
        File directory;

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output"));
        if (! directory.exists()){
            directory.mkdir();
        }

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output/cloudletResponseTime"));
        if (! directory.exists()){
            directory.mkdir();
        }

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output/cloudlet1ResponseTime"));
        if (! directory.exists()){
            directory.mkdir();
        }

        path = System.getProperty("user.dir");
        directory = new File(String.valueOf(path+"/output/cloudlet2ResponseTime"));
        if (! directory.exists()){
            directory.mkdir();
        }

    }

    public void updateArea() {

        n1 = controller.getCloudletService().getN1();
        n2 = controller.getCloudletService().getN2();
        cloud_n1 = controller.getCloudService().getN1();
        cloud_n2 = controller.getCloudService().getN2();

        number = n1 + n2 + cloud_n1 + cloud_n2;


        if (number > 0)  {                               /* update system integrals */
            systemArea.service += (clock.getNext() - clock.getCurrent());
        }
        if (n1 + n2 > 0)  {                              /* update cloudlet integrals */
            cloudletArea.node += (clock.getNext() - clock.getCurrent())*(n1 + n2);
            cloudletArea.service += (clock.getNext() - clock.getCurrent());
            cloudlet1Area.node += (clock.getNext() - clock.getCurrent())*n1;
            cloudlet2Area.node += (clock.getNext() - clock.getCurrent())*n2;
        }
        if (number - (n1 + n2) > 0)  {                   /* update cloud integrals */
            cloudArea.node += (clock.getNext() - clock.getCurrent())*(number - (n1 + n2));
            cloudArea.service += (clock.getNext() - clock.getCurrent());
            cloud1Area.node += (clock.getNext() - clock.getCurrent())*cloud_n1;
            cloud2Area.node += (clock.getNext() - clock.getCurrent())*cloud_n2;
        }
    }
}
