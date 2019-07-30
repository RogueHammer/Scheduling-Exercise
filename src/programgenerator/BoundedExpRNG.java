package programgenerator;
import java.util.Random;
/**
 * Write a description of class BoundedRNG here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BoundedExpRNG {
    private double lambda;
    private Random u_rng;
    
    public BoundedExpRNG(double lambda) {
        this.lambda=lambda;
        this.u_rng = new Random();
    }
    
    public double next() {
        final double U = u_rng.nextDouble();
        return -Math.log(1-U*(1-Math.exp(-lambda)) )/lambda;
    }
    
    public int nextInt(int lowerBound, int upperBound) {
        assert(lowerBound<=upperBound);
        return lowerBound+this.nextInt(upperBound-lowerBound);
    }
    
    public int nextInt(int upperBound) {
        return (int)Math.rint(upperBound*this.next());
    }
    
    public double getLambda() { return lambda; }
}
