package utils;

public class Clock {

    private static Clock instance = null;

    /* Last arrival time */
    private double arrival;

    /* Next Completion */
    //private double completion;

    /* Current time */
    private double current;

    /* Next-Event time*/
    private double next;

    private Clock() {
        //this.arrival = 0.0;
        //this.completion = 0.0;
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
     * Update Current Clock time with min between last arrival and next completion
     */
    //public void updateCurrent(){
    //    this.current = next;
    //    this.next = Double.min(this.arrival,this.completion);
    //}

    /**
     * Getter and Setter
     */
    public double getArrival() {
        return arrival;
    }
    public void setArrival(double arrival) {
        this.arrival = arrival;
    }

   //public double getCompletion() {
   //    return completion;
   //}

   //public void setCompletion(double completion) {
   //    this.completion = completion;
   //}

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
}
