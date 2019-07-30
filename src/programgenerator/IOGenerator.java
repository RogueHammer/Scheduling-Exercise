package programgenerator;
import java.util.Arrays;
import java.util.Random;
import simulator.IOInstruction;
/**
 * Write a description of class IOGenerator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IOGenerator extends InstructionGenerator {

    private final Integer[] devices;
    private final Random deviceSelector = new Random();
    
    public IOGenerator(BoundedExpRNG rng, Integer[] devices, int lowerBound, int upperBound) {
        super(rng, lowerBound, upperBound);
        this.devices=devices;
    }
    
    public IOInstruction make() {
        final int device = deviceSelector.nextInt(devices.length);
        return new IOInstruction(super.nextBurst(), devices[device]);
    }
    

    public String toString() { return "IO ("+super.toString()+", devices="+Arrays.deepToString(devices)+")"; }
    
}
