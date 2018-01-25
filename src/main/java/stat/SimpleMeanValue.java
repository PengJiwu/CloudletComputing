package stat;

public class SimpleMeanValue {

    private double current_mean;
    private double current_var;
    private double current_index;

    public SimpleMeanValue() {
        current_mean = 0.0;
        current_index = 0;
    }

    /**
     *
     * @return the mean at current state
     */
    public double getMean() {
        return current_mean;
    }

    /**
     *
     * @return the variance at current state
     */
    public double getVariance() {return current_var;}
    /**
     *
     * @param elem
     * @return
     */
    public SimpleMeanValue addElement(double elem) {
        current_index++;
        double prev_mean = current_mean;
        current_mean += (1/current_index)*(elem- current_mean);
        current_var += (current_index-1)/(current_index) * (elem - prev_mean) * (elem - prev_mean);
        return this;
    }

}
