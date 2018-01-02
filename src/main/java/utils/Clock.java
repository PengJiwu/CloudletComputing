package utils;

public class Clock {

    /* Last arrival time */
    private Double arrival;

    /* Next Completion */
    private Double completion;

    /* Current time */
    private Double current;

    /* Next-Event time*/
    private Double next;

    public Clock() {
        this.arrival = 0.0;
        this.completion = 0.0;
        this.current = 0.0;
        this.next = 0.0;
    }

    /**
     * Update Current Clock time with min between last arrival and next completion
     */
    public void updateCurrent(){
        this.current = next;
        this.next = Double.min(this.arrival,this.completion);
    }

    /**
     * Getter and Setter
     */
    public Double getArrival() {
        return arrival;
    }

    public void setArrival(Double arrival) {
        this.arrival = arrival;
    }

    public Double getCompletion() {
        return completion;
    }

    public void setCompletion(Double completion) {
        this.completion = completion;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getNext() {
        return next;
    }

    public void setNext(Double next) {
        this.next = next;
    }
}
