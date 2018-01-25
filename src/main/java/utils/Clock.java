package utils;

public class Clock {

    private static Clock instance = null;

    /* Last arrival time */
    private double arrival;

    /* Current time */
    private double current;

    /* Next-Event time*/
    private double next;

    private Clock() {
        this.current = 0.0;
        this.next = 0.0;
    }

    /**
     * Return singleton instance
     */
    public static Clock getInstance(){
        if (instance == null)
            instance = new Clock();
        return instance;
    }


    /**
     * Getter and Setter
     */
    public double getArrival() {
        return arrival;
    }

    public void setArrival(double arrival) {
        this.arrival = arrival;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getNext() {
        return next;
    }

    public void setNext(double next) {
        this.next = next;
    }

    public static void restart() {
        instance = null;
    }
}
