package utils;

import library.Rngs;
import library.Rvgs;

public class Distributions {

    private static Distributions instance = null;

    private Rvgs rvgs;
    private Rngs rngs;

    private Distributions() {
        rngs = new Rngs();
        rngs.plantSeeds(123456789);
        rvgs = new Rvgs(this.rngs);
    }

    /**
     * Return singleton instance
     */
    public static Distributions getInstance(){
        if (instance == null)
            instance = new Distributions();
        return instance;
    }

    public void selectStream(int index){
        rvgs.rngs.selectStream(index);
    }

    public double exponential(double param){
        return this.rvgs.exponential(1/param);
    }

    public double uniform(double a, double b){
        return rvgs.uniform(a, b);
    }

    /*
    public static void main(String[] args) {
        Distributions d = Distributions.getInstance();
        double arrival = 0;
        for(int i = 0; i<20; i++) {
            arrival = d.exponential((double) 1/0.45);
            System.out.println(arrival);
        }
    }*/
}

