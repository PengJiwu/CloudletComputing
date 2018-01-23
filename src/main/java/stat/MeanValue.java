package stat;

/**
 * MeanValue implements the Welford's algorithm for compute weighted mean value of a sample
 */
public class MeanValue {

    private double current_value;

    public MeanValue() {
        current_value = 0.0;
    }

    /**
     *
     * @return the mean at current state
     */
    public double getMean() {
        return current_value;
    }

    /**
     *
     * @param elem the element to add for compute the mean
     * @param cur_time the current time event
     * @param prev_time the previus time event
     * @return
     */
    public MeanValue addElement(double elem, double cur_time, double prev_time) {
        double delta = cur_time - prev_time;
        if (delta > 0) {
            current_value += (delta/cur_time)*(elem-current_value);
        }
        return this;
    }

}
